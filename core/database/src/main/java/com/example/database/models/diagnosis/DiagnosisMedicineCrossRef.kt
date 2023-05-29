package com.example.database.models.diagnosis

import androidx.room.Entity

@Entity(tableName = "diagnosisMedicinesCrossRef", primaryKeys = ["diagnosisId", "medicineId"])
data class DiagnosisMedicineCrossRef(
    val diagnosisId: String,
    val medicineId: String,
)
