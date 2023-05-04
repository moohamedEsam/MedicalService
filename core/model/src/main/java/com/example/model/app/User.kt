package com.example.model.app

import kotlinx.serialization.Serializable

sealed interface User {
    val username: String
    val email: String
    val phone: String
    val type: UserType
    val location: Location
    val id: String

    @Serializable
    data class Receiver(
        override val username: String,
        override val email: String,
        override val phone: String,
        override val type: UserType,
        override val location: Location,
        override val id: String,
        val medicalPrescriptionUrl: String,
        val salaryProofUrl: String,
        val idProofUrl: String,
        val diseases: List<Disease>,
        val suggestedMedicines: List<Medicine>,
    ) : User

    @Serializable
    data class Donor(
        override val username: String,
        override val email: String,
        override val phone: String,
        override val type: UserType,
        override val location: Location,
        override val id: String,
    ) : User

    companion object
}

fun User.Companion.emptyReceiver() = User.Receiver(
    username = "",
    email = "",
    phone = "",
    type = UserType.Receiver,
    location = Location(0.0, 0.0),
    id = "",
    medicalPrescriptionUrl = "",
    salaryProofUrl = "",
    idProofUrl = "",
    diseases = emptyList(),
    suggestedMedicines = emptyList(),
)

fun User.Companion.emptyDonor() = User.Donor(
    username = "",
    email = "",
    phone = "",
    type = UserType.Donner,
    location = Location(0.0, 0.0),
    id = "",
)

