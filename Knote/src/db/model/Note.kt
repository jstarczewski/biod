package com.jstarczewski.knote.db.model

data class Note(
    val id: Long,
    val author: String,
    val title: String,
    val content: String,
    val isPublic: Boolean = false
)