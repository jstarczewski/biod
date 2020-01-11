package com.jstarczewski.knote

import com.jstarczewski.knote.routes.*
import com.jstarczewski.knote.util.Injection
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.freemarker.FreeMarker
import io.ktor.http.ContentType
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex


@Location("/")
class Index

@Location("/styles/main.css")
class MainCss

@Location("/user")
class UserPage(val error: String = "")

@Location("/user/note")
class AddNote(val error: String = "")

@Location("/user/note/delete/{id}")
class DeleteNote(val id: Long)

@Location("/login")
data class Login(val userId: String = "", val error: String = "")

@Location("/logout")
class Logout

data class KnoteSession(val userId: String)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private const val UPLOAD_DIR_CONFIG_PATH = "Knote"
private const val SESSION_NAME = "SESSION_LOG"
private const val BASE_PACKAGE_PATH = "templates"

@KtorExperimentalAPI
private val hashKey = hex("6819b57a326945c1968f45236587")

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val uploadDir = Injection.provideUploadDir(environment.config.config(UPLOAD_DIR_CONFIG_PATH))
    val userDb = Injection.provideUserDataSource()
    val notesDb = Injection.provideNotesDataSource(uploadDir)

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(Locations)
    install(Sessions) {
        cookie<KnoteSession>(
            SESSION_NAME
        ) {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    routing {
        styles()
        index(notesDb)
        login(userDb)
        logout()
        userPage(userDb, notesDb)
        addNote(userDb, notesDb)
        deleteNote(userDb, notesDb)

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
    }
}

