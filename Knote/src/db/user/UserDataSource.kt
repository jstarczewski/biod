package com.jstarczewski.knote.routes.db.user

import com.jstarczewski.knote.db.model.User

interface UserDataSource {

    fun userByLogin(login: String): User?

    fun userById(id: String): User?

    fun user(login: String, password: String): User?
}