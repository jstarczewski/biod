package com.jstarczewski.knote.password

interface Validator {

    fun validate(password: String): String?

    fun getValidationErrorMessage(): String
}