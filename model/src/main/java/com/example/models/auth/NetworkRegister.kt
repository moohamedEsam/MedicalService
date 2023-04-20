package com.example.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class NetworkRegister(
    val username: String,
    val email: String,
    val password: String,
    val phone: String,
    val type: Int,
    val location: Location,
)

fun Register.toNetworkRegister(): NetworkRegister =
    NetworkRegister(
        username = username,
        email = email,
        password = password,
        phone = phone,
        type = type.ordinal,
        location = location,
    )

