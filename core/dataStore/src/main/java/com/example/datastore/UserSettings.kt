package com.example.datastore

import com.example.model.app.user.UserType
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val email: String = "",
    val password: String = "",
    val type: UserType = UserType.Donner,
    val id: String = "",
    val token: String = ""
)