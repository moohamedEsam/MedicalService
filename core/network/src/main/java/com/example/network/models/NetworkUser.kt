package com.example.network.models

import com.example.model.app.user.Location
import com.example.model.app.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    @SerialName("userName")
    val username: String,
    val email: String,
    val phone: String,
    val location: Location,
    val id: String,
    @SerialName("enumType")
    val type: String,
)

fun NetworkUser.toUser(): User = when (type) {
    "USER" -> User.Receiver(
        username = username,
        email = email,
        phone = phone,
        location = location,
        id = id,
    )

    "DONATOR" -> User.Donor(
        username = username,
        email = email,
        phone = phone,
        location = location,
        id = id,
    )

    "Doctor" -> User.Doctor(
        username = username,
        email = email,
        phone = phone,
        location = location,
        id = id,
    )

    else -> throw IllegalArgumentException("Unknown user type: $type")
}