package com.example.model.app.medicine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Medicine(
    val name: String,
    val sideEffects: List<String>,
    val precautions: List<String>,
    @SerialName("overDos")
    val overdoes: List<String>,
    val uses: List<String> = emptyList(),
    @SerialName("diseasesID")
    val diseasesId: List<String> = emptyList(),
    val id: String,
) {
    companion object
}

fun Medicine.Companion.empty() = Medicine(
    name = "",
    sideEffects = emptyList(),
    precautions = emptyList(),
    overdoes = emptyList(),
    id = UUID.randomUUID().toString(),
    uses = emptyList(),
)