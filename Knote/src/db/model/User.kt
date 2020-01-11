package com.jstarczewski.knote.db.model

data class User(
    val userId: String,
    val login: String,
    val password: String
)