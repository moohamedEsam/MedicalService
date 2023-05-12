package com.example.model.app.disease

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Disease(
    val id: String,
    val name: String,
    val description: String,
    @SerialName("symptomsList")
    val symptoms: List<String>,
    @SerialName("treatmentList")
    val treatment: List<String>,
    @SerialName("preventionList")
    val prevention: List<String>,
    @SerialName("diagnosisList")
    val diagnosis: List<String>
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