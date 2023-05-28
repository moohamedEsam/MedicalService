package com.example.auth.login

sealed interface LoginScreenEvent {
    data class EmailChanged(val value: String) : LoginScreenEvent
    data class PasswordChanged(val value: String) : LoginScreenEvent
    object LoginClicked : LoginScreenEvent

    object RegisterClicked : LoginScreenEvent

    object OnSettingsClick : LoginScreenEvent
}
