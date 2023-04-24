package com.example.auth.login

import com.example.common.models.ValidationResult

data class LoginScreenState(
    val email: String = "",
    val emailValidationResult: ValidationResult = ValidationResult.Empty,
    val password: String = "",
    val passwordValidationResult: ValidationResult = ValidationResult.Empty,
    val isLoading: Boolean = false
) {
    val loginEnabled: Boolean
        get() = emailValidationResult is ValidationResult.Valid && passwordValidationResult is ValidationResult.Valid && !isLoading
}
