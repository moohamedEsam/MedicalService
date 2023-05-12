package com.example.database.models.disease

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.medicine.toMedicine
import com.example.model.app.disease.DiseaseView

data class DiseaseEntityView(
    @Embedded val disease: DiseaseEntity,
    @Relation(
        entity = MedicineEntity::class,
        associateBy = Junction(
            value = DiseaseMedicineCrossRef::class,
            parentColumn = "diseaseId",
            entityColumn = "medicineId",
        ),
        entityColumn = "id",
        parentColumn = "id",
    )
    val medicines: List<MedicineEntity>,

    )

fun DiseaseEntityView.toDiseaseView(): DiseaseView = DiseaseView(
    name = disease.name,
    description = disease.description,
    symptoms = disease.symptoms,
    treatment = disease.treatment,
    prevention = disease.prevention,
    diagnosis = disease.diagnosis,
    medicines = medicines.map(MedicineEntity::toMedicine),
    id = disease.id,
)