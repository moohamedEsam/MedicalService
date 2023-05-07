package com.example.model.app

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
    val id: String,
    val description: String = "",
) {
    companion object
}

fun Medicine.Companion.empty() = Medicine(
    name = "",
    sideEffects = emptyList(),
    precautions = emptyList(),
    overdoes = emptyList(),
    id = UUID.randomUUID().toString(),
    description = "",
)