package com.example.model.app.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface User {
    val username: String
    val email: String
    val phone: String
    val location: Location
    val id: String
    val type: UserType

    @Serializable
    data class Receiver(
        override val username: String,
        override val email: String,
        override val phone: String,
        override val location: Location,
        override val id: String,
    ) : User{
        override val type: UserType
            get() = UserType.Receiver
    }

    @Serializable
    data class Donor(
        override val username: String,
        override val email: String,
        override val phone: String,
        override val location: Location,
        override val id: String,
    ) : User{
        override val type: UserType
            get() = UserType.Donner
    }

    data class Doctor(
        override val username: String,
        override val email: String,
        override val phone: String,
        override val location: Location,
        override val id: String,
    ) : User{
        override val type: UserType
            get() = UserType.Doctor
    }

    @Serializable
    data class Unknown(
        val username: String,
        val email: String,
        val phone: String,
        val location: Location,
        val id: String,
        @SerialName("enumType")
        val type: String,
    )

    companion object
}

fun User.Companion.emptyReceiver() = User.Receiver(
    username = "",
    email = "",
    phone = "",
    location = Location(0.0, 0.0),
    id = "",
)

fun User.Companion.emptyDonor() = User.Donor(
    username = "",
    email = "",
    phone = "",
    location = Location(0.0, 0.0),
    id = "",
)

fun User.Companion.emptyDoctor() = User.Doctor(
    username = "",
    email = "",
    phone = "",
    location = Location(0.0, 0.0),
    id = "",
)

fun User.Unknown.toUser(): User = when (type) {
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