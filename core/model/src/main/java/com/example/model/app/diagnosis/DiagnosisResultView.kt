package com.example.model.app.diagnosis

import com.example.model.app.medicine.MedicineView
import com.example.model.app.user.User
import com.example.model.app.user.emptyDoctor
import java.util.Date
import java.util.UUID

data class DiagnosisResultView(
    val diagnosis: String,
    val doctor: User.Doctor?,
    val status: DiagnosisResult.Status,
    val id: String,
    val createdAt: Date,
    val updatedAt: Date,
    val request: DiagnosisRequest,
    val medications: List<MedicineView> = emptyList(),
) {
    companion object
}

fun DiagnosisResultView.Companion.empty() = DiagnosisResultView(
    diagnosis = "",
    doctor = User.emptyDoctor(),
    status = DiagnosisResult.Status.Pending,
    id = UUID.randomUUID().toString(),
    createdAt = Date(),
    updatedAt = Date(),
    request = DiagnosisRequest.empty(),
)
