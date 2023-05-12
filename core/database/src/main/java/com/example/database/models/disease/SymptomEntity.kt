package com.example.database.models.disease

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.disease.Symptom

@JvmInline
@Entity(tableName = "symptoms")
value class SymptomEntity(@PrimaryKey val value: String)

fun SymptomEntity.toSymptom() = Symptom(name = value)

fun Symptom.toSymptomEntity() = SymptomEntity(value = name)
