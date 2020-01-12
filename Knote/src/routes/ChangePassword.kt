package com.jstarczewski.knote.routes

import com.jstarczewski.knote.ChangePassword
import com.jstarczewski.knote.Index
import com.jstarczewski.knote.KnoteSession
import com.jstarczewski.knote.UserPage
import com.jstarczewski.knote.db.user.UserDataSource
import com.jstarczewski.knote.password.Validator
import com.jstarczewski.knote.util.redirect
import com.jstarczewski.knote.util.validateEquality
import com.jstarczewski.knote.util.withSession
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

private const val EQUALITY_ERROR = "Passwords must be equal"
private const val OLD_PASSWORD_ERROR = "Old password is wrong"

fun Routing.changePassword(userDb: UserDataSource, validators: List<Validator>) {

    get<ChangePassword> { pipeline ->
        with(call) {
            sessions.get<KnoteSession>()?.let {
                respond(
                    FreeMarkerContent(
                        "password.ftl", mapOf(
                            "error" to pipeline.error
                        ), ""
                    )
                )
            } ?: run {
                redirect(Index())
            }
        }
    }

    post<ChangePassword> {
        withSession(userDb) { user ->
            val post = call.receive<Parameters>()
            val oldPassword = post["old_password"]
            val password = post["new_password"]
            val repeatPassword = post["repeat_password"]
            password?.run {
                val error = ChangePassword(user.login)
                if (user.password != oldPassword) {
                    call.redirect(error.copy(error = OLD_PASSWORD_ERROR))
                }
                if (validateEquality(this, repeatPassword) == null) {
                    call.redirect(error.copy(error = EQUALITY_ERROR))
                }
                validators.forEach {
                    if (it.validate(this) == null) {
                        call.redirect(error.copy(error = it.getValidationErrorMessage()))
                    }
                }
                userDb.changePassword(user.userId, user.login, password)
                call.redirect(UserPage())
            } ?: run {
                call.redirect(UserPage())
            }
        } ?: run {
            call.redirect(Index())
        }
    }
}