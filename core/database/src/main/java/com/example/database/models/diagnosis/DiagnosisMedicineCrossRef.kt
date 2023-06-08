package com.example.database.models.diagnosis

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "diagnosisMedicinesCrossRef",
    primaryKeys = ["diagnosisId", "medicineId"],
    indices = [Index("diagnosisId"), Index("medicineId")]
)
data class DiagnosisMedicineCrossRef(
    val diagnosisId: String,
    val medicineId: String,
)
