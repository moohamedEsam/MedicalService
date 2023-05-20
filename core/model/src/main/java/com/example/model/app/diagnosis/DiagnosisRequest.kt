package com.example.model.app.diagnosis

import com.example.model.app.disease.Symptom
import com.example.model.serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
data class DiagnosisRequest(
    val symptoms: List<Symptom>,
    val description: String,
    val id: String = UUID.randomUUID().toString(),
    @Serializable(with = DateSerializer::class)
    val date: Date = Date(),
) {
    companion object
}

fun DiagnosisRequest.Companion.empty() = DiagnosisRequest(
    symptoms = emptyList(),
    description = "",
    id = UUID.randomUUID().toString()
)
