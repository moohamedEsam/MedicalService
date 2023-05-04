package com.example.model.app

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Disease(
    val id: String,
    val name: String,
    val description: String,
    val symptoms: List<String>,
    val treatment: List<String>,
    val prevention: List<String>,
    val diagnosis: List<String>,
){
    companion object
}


fun Disease.Companion.empty() = Disease(
    id = UUID.randomUUID().toString(),
    name = "",
    description = "",
    symptoms = emptyList(),
    treatment = emptyList(),
    prevention = emptyList(),
    diagnosis = emptyList(),
)