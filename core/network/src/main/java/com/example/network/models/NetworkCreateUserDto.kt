package com.example.network.models

import com.example.model.app.user.Location
import com.example.model.app.user.CreateUserDto
import com.example.model.app.user.UserType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRegister(
    val username: String,
    val email: String,
    val password: String,
    val phone: String,
    @SerialName("enumType")
    val type: String,
    val location: Location,
)

fun CreateUserDto.toNetworkCreateUserDto(): NetworkRegister =
    NetworkRegister(
        username = username,
        email = email,
        password = password,
        phone = phone,
        type = when (type) {
            UserType.Donner -> "DONATOR"
            UserType.Receiver -> "USER"
            UserType.Doctor -> "Doctor"
        },
        location = location,
    )

