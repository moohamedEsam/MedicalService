package com.example.database.models

data class MedicineEntityView(
    val name: String,
    val description: String,
    val sideEffects: List<String>,
    val precautions: List<String>,
    val overDoes: String,
    val diseases: List<DiseaseEntity>,
    val id: String,
)