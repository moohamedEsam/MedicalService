package com.example.database.models

data class DiseaseEntityView(
    val name: String,
    val description: String,
    val symptoms: List<String>,
    val treatment: List<String>,
    val prevention: List<String>,
    val diagnosis: List<String>,
    val medicines: List<MedicineEntity>,
    val id: String,
)
