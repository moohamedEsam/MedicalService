package com.example.models

import com.example.models.auth.Location
import com.example.models.auth.UserType
import kotlinx.serialization.Serializable

@Serializable
sealed interface User {
    val username: String
    val email: String
    val phone: String
    val type: UserType
    val location: Location
    val recentTransactions: List<Transaction>
    val id: String

    data class Receiver(
        override val username: String,
        override val email: String,
        override val phone: String,
        override val type: UserType,
        override val location: Location,
        override val recentTransactions: List<Transaction>,
        override val id: String,
        val medicalPrescriptionUrl: String,
        val salaryProofUrl: String,
        val idProofUrl: String,
        val diseases: List<Disease>,
        val suggestedMedicines: List<Medicine>,
    ) : User

    data class Donor(
        override val username: String,
        override val email: String,
        override val phone: String,
        override val type: UserType,
        override val location: Location,
        override val recentTransactions: List<Transaction>,
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
    recentTransactions = emptyList(),
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
    recentTransactions = emptyList(),
    id = "",
)

