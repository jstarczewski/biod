package com.jstarczewski.knote.routes

import com.jstarczewski.knote.Index
import com.jstarczewski.knote.UserPage
import com.jstarczewski.knote.db.notes.NotesDataSource
import com.jstarczewski.knote.routes.db.user.UserDataSource
import com.jstarczewski.knote.util.redirect
import com.jstarczewski.knote.util.withSession
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing

fun Routing.userPage(userDb: UserDataSource, notesDb: NotesDataSource) {

    get<UserPage> {
        withSession(userDb) { user ->
            val notes = notesDb.getAllNotes().toList()
            call.respond(
                FreeMarkerContent(
                    "userpage.ftl",
                    mapOf("all" to notes, "user" to user, "error" to it.error)
                )
            )
        } ?: run {
            call.redirect(Index())
        }
    }
}