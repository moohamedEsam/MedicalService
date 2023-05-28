package com.example.datastore

import com.example.model.app.user.UserType
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val email: String = "",
    val type: UserType = UserType.Donner,
    val token: String = "",
    val remoteServerIp: String = "192.168.1.2:3000",
)