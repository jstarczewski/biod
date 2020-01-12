package com.jstarczewski.knote.password.validators

import com.jstarczewski.knote.password.Validator

class NumberValidator : Validator {

    companion object {

        private const val NUMBER_ERROR = "Password must contain number"
    }

    override fun validate(password: String): String? =
        if (password.matches(Regex(".*\\d.*"))) password else null

    override fun getValidationErrorMessage(): String =
        NUMBER_ERROR
}