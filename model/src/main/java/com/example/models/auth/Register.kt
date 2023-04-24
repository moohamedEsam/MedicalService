package com.example.models.auth

import com.example.models.Location
import kotlinx.serialization.Serializable

@Serializable
data class Register(
    val username: String,
    val email: String,
    val password: String,
    val phone:String,
    val type:UserType,
    val location: Location,
    val medicalPrescriptionPath:String,
    val salaryProofPath:String,
    val idProofPath:String,
)
