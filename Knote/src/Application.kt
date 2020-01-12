package com.jstarczewski.knote

import com.jstarczewski.knote.password.validators.CapitalLetterValidator
import com.jstarczewski.knote.password.validators.LengthValidator
import com.jstarczewski.knote.password.validators.LetterValidator
import com.jstarczewski.knote.password.validators.NumberValidator
import com.jstarczewski.knote.routes.*
import com.jstarczewski.knote.util.Injection
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.freemarker.FreeMarker
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.Locations
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

@Location("/register")
data class Register(val login: String = "", val error: String = "")

@Location("user/password")
data class ChangePassword(val error: String = "")

@Location("/logout")
class Logout

data class KnoteSession(val userId: Long)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private const val UPLOAD_DIR_CONFIG_PATH = "Knote"
private const val SESSION_NAME = "SESSION_LOG"
private const val BASE_PACKAGE_PATH = "templates"

private const val USERS_DIR = "user.dir"
private const val NOTES_DIR = "note.dir"

@KtorExperimentalAPI
private val hashKey = hex("6819b57a326945c1968f45236587")

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val notesUploadDir = Injection.provideUploadDir(environment.config.config(UPLOAD_DIR_CONFIG_PATH), NOTES_DIR)
    val usersUploadDir = Injection.provideUploadDir(environment.config.config(UPLOAD_DIR_CONFIG_PATH), USERS_DIR)
    val userDb = Injection.provideUserDataSource(usersUploadDir)
    val notesDb = Injection.provideNotesDataSource(notesUploadDir)

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
        register(
            userDb, listOf(
                LengthValidator(),
                NumberValidator(),
                LetterValidator(),
                CapitalLetterValidator()
            )
        )
        changePassword(
            userDb, listOf(
                LengthValidator(),
                NumberValidator(),
                LetterValidator(),
                CapitalLetterValidator()
            )
        )
    }
}

