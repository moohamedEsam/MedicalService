package com.example.model.app.diagnosis

import java.util.Date

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
}
