package com.jstarczewski.knote.routes

import com.jstarczewski.knote.KnoteSession
import com.jstarczewski.knote.Login
import com.jstarczewski.knote.UserPage
import com.jstarczewski.knote.db.user.UserDataSource
import com.jstarczewski.knote.util.redirect
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.Parameters
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun Routing.login(db: UserDataSource, hash: (String) -> String, delayGenerator: () -> Long) {

    get<Login> {
        with(call) {
            sessions.get<KnoteSession>()?.let {
                db.userById(it.userId)
            }?.run {
                redirect(UserPage())
            } ?: run {
                respond(FreeMarkerContent("login.ftl", mapOf("userId" to it.userId, "error" to it.error), ""))
            }
        }
    }

    post<Login> {
        val post = call.receive<Parameters>()
        val login = post["login"]
        val password = post["password"]
        login?.let { login ->
            password?.run {
                val error = Login(login)
                db.user(login, hash(this))?.let { user ->
                    runBlocking {
                        delay(delayGenerator())
                        call.sessions.set(KnoteSession(user.userId))
                        call.redirect(UserPage())
                    }
                } ?: run {
                    call.redirect(error.copy(error = "Invalid username or password"))
                }
            } ?: run {
                val error = Login(login)
                call.redirect(error.copy(error = "Unexpected Error appeared"))
            }
        } ?: run {
            val error = Login("null")
            call.redirect(error.copy(error = "Invalid username or password"))
        }
    }
}