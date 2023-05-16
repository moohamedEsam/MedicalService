package com.example.database.models.diagnosis

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.diagnosis.DiagnosisResult
import java.util.Date

@Entity(tableName = "diagnosisResults")
data class DiagnosisResultEntity(
    val diagnosis: String,
    val doctorId: String,
    val status: DiagnosisResult.Status,
    val createdAt: Date,
    val updatedAt: Date,
    val diagnosisRequestId: String,
    val isCreated: Boolean = false,
    val isDeleted: Boolean = false,
    val isUpdated: Boolean = false,
    @PrimaryKey val id: String,
)


fun DiagnosisResultEntity.toDiagnosisResult() = DiagnosisResult(
    id = id,
    diagnosis = diagnosis,
    doctorId = doctorId,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
    diagnosisRequestId = diagnosisRequestId,
)

fun DiagnosisResult.toEntity() = DiagnosisResultEntity(
    id = id,
    diagnosis = diagnosis,
    doctorId = doctorId,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
    diagnosisRequestId = diagnosisRequestId,
)