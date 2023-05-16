package com.example.database.models.diagnosis

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.database.models.user.UserEntity
import com.example.database.models.user.toUser
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.user.User
import com.example.model.app.user.emptyDoctor
import java.util.Date

data class DiagnosisResultEntityView(
    @Embedded val diagnosisResult: DiagnosisResultEntity,
    @Relation(
        parentColumn = "diagnosisRequestId",
        entityColumn = "id",
        entity = DiagnosisRequestEntity::class
    )
    val diagnosisRequest: DiagnosisRequestEntity,
    @Relation(
        parentColumn = "doctorId",
        entityColumn = "id",
        entity = UserEntity::class
    )
    val doctor: UserEntity
)

fun DiagnosisResultEntityView.toDiagnosisResultView() = DiagnosisResultView(
    id = diagnosisResult.id,
    createdAt = diagnosisResult.createdAt,
    updatedAt = diagnosisResult.updatedAt,
    diagnosis = diagnosisResult.diagnosis,
    doctor = (doctor.toUser() as? User.Doctor) ?: User.emptyDoctor(),
    status = diagnosisResult.status,
    diagnosisRequest = diagnosisRequest.toDiagnosisRequest(),
)
