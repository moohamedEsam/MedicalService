package com.example.auth.login

sealed interface LoginScreenEvent {
    data class EmailChanged(val value: String) : LoginScreenEvent
    data class PasswordChanged(val value: String) : LoginScreenEvent
    data class LoginClicked(val onSuccess: () -> Unit) : LoginScreenEvent
}
