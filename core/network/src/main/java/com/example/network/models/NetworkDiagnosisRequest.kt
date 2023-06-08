package com.example.network.models

import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.disease.Symptom
import com.example.model.serializers.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
data class NetworkDiagnosisRequest(
    val symptoms: List<String>,
    val description: String,
    val id: String = UUID.randomUUID().toString(),
    @Serializable(with = DateSerializer::class)
    val date: Date = Date(),
)

fun NetworkDiagnosisRequest.toDiagnosisResult() = DiagnosisRequest(
    symptoms = symptoms.map { Symptom(it) },
    description = description,
    id = id,
    date = date,
)

fun DiagnosisRequest.toNetwork() = NetworkDiagnosisRequest(
    symptoms = symptoms.map { it.name },
    description = description,
    id = id,
    date = date,
)
