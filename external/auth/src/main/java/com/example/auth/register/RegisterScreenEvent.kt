package com.example.auth.register

import com.example.model.app.Location
import com.example.model.app.UserType


sealed interface RegisterScreenEvent {
    data class EmailChanged(val value: String) : RegisterScreenEvent
    data class PasswordChanged(val value: String) : RegisterScreenEvent
    data class PasswordConfirmationChanged(val value: String) : RegisterScreenEvent
    data class UsernameChanged(val value: String) : RegisterScreenEvent
    data class PhoneChanged(val value: String) : RegisterScreenEvent
    data class LocationChanged(val value: Location) : RegisterScreenEvent
    data class UserTypeChanged(val value: UserType) : RegisterScreenEvent
    object RegisterClicked : RegisterScreenEvent

    object LoginClicked : RegisterScreenEvent

    object LocationClicked : RegisterScreenEvent
}