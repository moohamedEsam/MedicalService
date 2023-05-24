package com.example.model.app.diagnosis

import java.util.Date
import java.util.UUID

data class DiagnosisResult(
    val diagnosis: String,
    val doctorId: String,
    val status: Status,
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val diagnosisRequestId: String,
) {
    enum class Status {
        Pending,
        Complete,
        InProgress,
    }

    companion object
}

fun DiagnosisResult.Companion.empty() = DiagnosisResult(
    diagnosis = "",
    doctorId = "",
    status = DiagnosisResult.Status.Pending,
    id = UUID.randomUUID().toString(),
    createdAt = Date(),
    updatedAt = Date(),
    diagnosisRequestId = "",
)
