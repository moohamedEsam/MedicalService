package com.example.network.models

import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.serializers.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NetworkDiagnosisResult(
    @SerialName("diagnoses")
    val diagnosis: String,
    val doctorId: String? = "",
    @SerialName("state")
    val status: String,
    val id: String,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date,
    val diagnosisRequestId: String,
    val userId: String = "",
    @SerialName("medicationId")
    val medicationsIds: List<String> = emptyList(),
    val diseaseId: String = "",
)

fun DiagnosisResult.toNetwork() = NetworkDiagnosisResult(
    diagnosis = diagnosis,
    doctorId = doctorId,
    status = status.name.uppercase(),
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    diagnosisRequestId = diagnosisRequestId,
    medicationsIds = medicationsIds,
    diseaseId = diseaseId
)

fun NetworkDiagnosisResult.toDiagnosisResult() = DiagnosisResult(
    diagnosis = diagnosis,
    doctorId = doctorId ?: "",
    status = when (status) {
        "PENDING" -> DiagnosisResult.Status.Pending
        "INPROGRESS" -> DiagnosisResult.Status.InProgress
        "COMPLETE" -> DiagnosisResult.Status.Complete

        else -> throw IllegalArgumentException("Unknown status: $status")
    },
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    diagnosisRequestId = diagnosisRequestId,
    medicationsIds = medicationsIds,
    diseaseId = diseaseId
)