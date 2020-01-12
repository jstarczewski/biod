package com.jstarczewski.knote.password.validators

import com.jstarczewski.knote.password.Validator

class LetterValidator : Validator {

    companion object {

        private const val LETTER_VALIDATOR = "Password must contain lower-case letter"
    }

    override fun validate(password: String) =
        if (password.matches(Regex(".*[a-z].*"))) password else null

    override fun getValidationErrorMessage() =
        LETTER_VALIDATOR
}