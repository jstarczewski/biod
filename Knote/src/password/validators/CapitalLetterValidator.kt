package com.jstarczewski.knote.password.validators

import com.jstarczewski.knote.password.Validator

class CapitalLetterValidator : Validator {

    companion object {

        private const val CAPITAL_LETTER_ERROR = "Password must contain at leas one capital letter"
    }

    override fun validate(password: String) =
        if (password.matches(Regex(".*[A-Z].*"))) password else null

    override fun getValidationErrorMessage() =
        CAPITAL_LETTER_ERROR
}