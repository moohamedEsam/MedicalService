package com.example.model.app.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

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

    companion object
}

fun User.Companion.emptyReceiver() = User.Receiver(
    username = "",
    email = "",
    phone = "",
    location = Location(0.0, 0.0),
    id = UUID.randomUUID().toString(),
)

fun User.Companion.emptyDonor() = User.Donor(
    username = "",
    email = "",
    phone = "",
    location = Location(0.0, 0.0),
    id = UUID.randomUUID().toString(),
)

fun User.Companion.emptyDoctor() = User.Doctor(
    username = "",
    email = "",
    phone = "",
    location = Location(0.0, 0.0),
    id = UUID.randomUUID().toString(),
)