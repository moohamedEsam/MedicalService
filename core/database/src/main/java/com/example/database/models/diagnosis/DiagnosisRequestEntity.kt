package com.example.database.models.diagnosis

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.disease.Symptom
import java.util.Date
import java.util.UUID

@Entity(tableName = "diagnosisRequests")
data class DiagnosisRequestEntity(
    val symptoms: List<Symptom>,
    val description: String,
    val isCreated: Boolean = false,
    val isUpdated: Boolean = false,
    val isDeleted: Boolean = false,
    val date: Date = Date(),
    @PrimaryKey val id: String = UUID.randomUUID().toString()
)

fun DiagnosisRequestEntity.toDiagnosisRequest() = DiagnosisRequest(
    id = id,
    symptoms = symptoms,
    description = description,
    date = date
)

fun DiagnosisRequest.toEntity() = DiagnosisRequestEntity(
    id = id,
    symptoms = symptoms,
    description = description,
    date = date
)