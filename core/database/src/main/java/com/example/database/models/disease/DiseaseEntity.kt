package com.example.database.models.disease

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.Disease

@Entity(tableName = "diseases")
data class DiseaseEntity(
    val name: String,
    val description: String,
    val symptoms: List<String>,
    val treatment: List<String>,
    val prevention: List<String>,
    val diagnosis: List<String>,
    @PrimaryKey val id: String,
)

fun DiseaseEntity.toDisease() = Disease(
    id = id,
    name = name,
    description = description,
    symptoms = symptoms,
    treatment = treatment,
    prevention = prevention,
    diagnosis = diagnosis,
)

fun Disease.toEntity() = DiseaseEntity(
    id = id,
    name = name,
    description = description,
    symptoms = symptoms,
    treatment = treatment,
    prevention = prevention,
    diagnosis = diagnosis,
)