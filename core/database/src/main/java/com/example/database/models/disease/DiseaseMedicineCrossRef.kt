package com.example.database.models.disease

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "diseaseMedicineCrossRef",
    primaryKeys = ["diseaseId", "medicineId"],
    indices = [Index("medicineId"), Index("diseaseId")]
)
data class DiseaseMedicineCrossRef(
    val diseaseId: String,
    val medicineId: String,
)
