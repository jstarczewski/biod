package com.jstarczewski.knote.db.model

import java.io.Serializable

data class User(
    val userId: Long,
    val login: String,
    val password: String
) : Serializable