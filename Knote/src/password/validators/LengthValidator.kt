package com.jstarczewski.knote.password.validators

import com.jstarczewski.knote.password.Validator

class LengthValidator : Validator {

    companion object {

        private const val LENGTH_ERROR = "Password is too short"
        private const val MIN_LENGTH = 8
    }

    override fun validate(password: String): String? =
        if (password.length >= MIN_LENGTH) password else null

    override fun getValidationErrorMessage(): String =
        LENGTH_ERROR
}