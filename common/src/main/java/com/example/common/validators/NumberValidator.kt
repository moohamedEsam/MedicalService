package com.example.common.validators

import com.example.common.models.ValidationResult

fun validateNumber(value: String) = when {
    value.isBlank() -> {
        ValidationResult.Empty
    }
    else -> {
        if (value.toFloatOrNull() == null)
            ValidationResult.Invalid("Invalid Value")
        else
            ValidationResult.Valid
    }
}