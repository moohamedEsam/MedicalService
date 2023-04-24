package com.example.auth.login

import com.example.common.models.dataType.Password
import com.example.common.models.ValidationResult
import com.example.common.models.dataType.Email

data class LoginScreenState(
    val email: Email = Email(""),
    val password: Password = Password(""),
    val isLoading: Boolean = false
) {
    val loginEnabled: Boolean
        get() = email.validationResult is ValidationResult.Valid && password.validationResult is ValidationResult.Valid && !isLoading
}
