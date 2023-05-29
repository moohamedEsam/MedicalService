package com.example.database.models.diagnosis

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.database.models.disease.DiseaseEntity
import com.example.database.models.disease.DiseaseEntityView
import com.example.database.models.disease.toDiseaseView
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.medicine.MedicineEntityView
import com.example.database.models.medicine.toMedicine
import com.example.database.models.medicine.toMedicineView
import com.example.database.models.user.UserEntity
import com.example.database.models.user.toUser
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.medicine.MedicineView
import com.example.model.app.user.User
import com.example.model.app.user.emptyDoctor

data class DiagnosisResultEntityView(
    @Embedded val diagnosisResult: DiagnosisResultEntity,
    @Relation(
        parentColumn = "diagnosisRequestId",
        entityColumn = "id",
        entity = DiagnosisRequestEntity::class
    )
    val diagnosisRequest: DiagnosisRequestEntity,

    @Relation(
        entity = MedicineEntity::class,
        associateBy = Junction(
            value = DiagnosisMedicineCrossRef::class,
            parentColumn = "diagnosisId",
            entityColumn = "medicineId"
        ),
        parentColumn = "id",
        entityColumn = "id"
    )
    val medications: List<MedicineEntity>,
    @Relation(
        parentColumn = "diseaseId",
        entityColumn = "id",
        entity = DiseaseEntity::class
    )
    val disease: DiseaseEntityView?,
    @Relation(
        parentColumn = "doctorId",
        entityColumn = "id",
        entity = UserEntity::class
    )
    val doctor: UserEntity?
)

fun DiagnosisResultEntityView.toDiagnosisResultView() = DiagnosisResultView(
    id = diagnosisResult.id,
    createdAt = diagnosisResult.createdAt,
    updatedAt = diagnosisResult.updatedAt,
    diagnosis = diagnosisResult.diagnosis,
    doctor = (doctor?.toUser() as? User.Doctor),
    status = diagnosisResult.status,
    request = diagnosisRequest.toDiagnosisRequest(),
    medications = medications.map { it.toMedicine() },
    disease = disease?.toDiseaseView()
)
