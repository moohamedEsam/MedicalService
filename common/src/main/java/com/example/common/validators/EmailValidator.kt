package com.example.common.validators

import com.example.common.models.ValidationResult

fun validateEmail(email:String): ValidationResult = when {
    email.isEmpty() -> ValidationResult.Empty
    email.isBlank() -> ValidationResult.Invalid("Email cannot be blank")
    !Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}").matches(email) -> ValidationResult.Invalid("Invalid email")
    else -> ValidationResult.Valid
}