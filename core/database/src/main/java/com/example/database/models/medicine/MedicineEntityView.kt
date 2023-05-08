package com.example.database.models.medicine

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.database.models.disease.DiseaseEntity
import com.example.database.models.disease.DiseaseMedicineCrossRef
import com.example.database.models.disease.toDisease
import com.example.model.app.medicine.MedicineView

data class MedicineEntityView(
    @Embedded val medicineEntity: MedicineEntity,
    @Relation(
        entity = DiseaseEntity::class,
        associateBy = Junction(
            value = DiseaseMedicineCrossRef::class,
            parentColumn = "medicineId",
            entityColumn = "diseaseId"
        ),
        parentColumn = "id",
        entityColumn = "id"
    )
    val diseases: List<DiseaseEntity>,
)

fun MedicineEntityView.toMedicineView() = MedicineView(
    name = medicineEntity.name,
    uses = medicineEntity.uses,
    sideEffects = medicineEntity.sideEffects,
    precautions = medicineEntity.precautions,
    overDoes = medicineEntity.overDoes,
    diseases = diseases.map { it.toDisease() },
    id = medicineEntity.id,
)