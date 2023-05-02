package com.example.auth.register

import com.example.models.Location
import com.example.models.auth.UserType

sealed interface RegisterScreenEvent {
    data class EmailChanged(val value: String) : RegisterScreenEvent
    data class PasswordChanged(val value: String) : RegisterScreenEvent
    data class PasswordConfirmationChanged(val value: String) : RegisterScreenEvent
    data class UsernameChanged(val value: String) : RegisterScreenEvent
    data class PhoneChanged(val value: String) : RegisterScreenEvent
    data class LocationChanged(val value: Location) : RegisterScreenEvent
    data class UserTypeChanged(val value: UserType) : RegisterScreenEvent
    data class RegisterClicked(val onSuccess: () -> Unit) : RegisterScreenEvent
}