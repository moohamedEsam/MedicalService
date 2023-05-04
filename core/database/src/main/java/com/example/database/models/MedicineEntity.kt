package com.example.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.Medicine

@Entity(tableName = "medicines")
data class MedicineEntity(
    val name: String,
    val description: String,
    val sideEffects: List<String>,
    val precautions: List<String>,
    val overDoes: String,
    @PrimaryKey val id:String,
)

fun MedicineEntity.toMedicine() = Medicine(
    id = id,
    name = name,
    description = description,
    sideEffects = sideEffects,
    precautions = precautions,
    overDoes = overDoes,
)

fun Medicine.toEntity() = MedicineEntity(
    id = id,
    name = name,
    description = description,
    sideEffects = sideEffects,
    precautions = precautions,
    overDoes = overDoes,
)
