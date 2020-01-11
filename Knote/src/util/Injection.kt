package com.jstarczewski.knote.util

import com.jstarczewski.knote.db.notes.NotesDatabase
import com.jstarczewski.log.db.UserDatabase
import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import java.io.File
import java.io.IOException

object Injection {

    private const val NOTES_DIR = "note.dir"

    @KtorExperimentalAPI
    fun provideUploadDir(config: ApplicationConfig): File {
        val uploadDirPath: String = config.property(NOTES_DIR).getString()
        val uploadDir = File(uploadDirPath)
        if (!uploadDir.mkdirs() && !uploadDir.exists()) {
            throw IOException("Failed to create directory ${uploadDir.absolutePath}")
        }
        return uploadDir
    }

    fun provideUserDataSource() = UserDatabase()

    fun provideNotesDataSource(uploadDir: File) = NotesDatabase(uploadDir)
}