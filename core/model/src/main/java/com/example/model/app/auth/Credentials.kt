package com.example.model.app.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    @SerialName("username")
    val email: String,
    val password: String
)
