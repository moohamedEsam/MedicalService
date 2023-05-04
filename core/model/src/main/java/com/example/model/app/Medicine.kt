package com.example.model.app

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Medicine(
    val name: String,
    val description: String,
    val sideEffects: List<String>,
    val precautions: List<String>,
    val overDoes: String,
    val id:String,
){
    companion object
}

fun Medicine.Companion.empty() = Medicine(
    name = "",
    sideEffects = emptyList(),
    precautions = emptyList(),
    overDoes = "",
    id = UUID.randomUUID().toString(),
    description = "",
)