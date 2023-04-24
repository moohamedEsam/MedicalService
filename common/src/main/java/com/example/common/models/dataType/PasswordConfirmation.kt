package com.example.common.models.dataType

import com.example.common.models.ValidationResult

@JvmInline
value class PasswordConfirmation(val value: String) {
    fun validate(password: String): ValidationResult {
        return if (value.isEmpty())
            ValidationResult.Empty
        else if (value == password)
            ValidationResult.Valid
        else
            ValidationResult.Invalid("Passwords do not match")

    }
}