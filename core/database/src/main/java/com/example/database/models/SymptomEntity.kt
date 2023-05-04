package com.example.database.models

import androidx.room.Entity
import com.example.model.app.Symptom

@JvmInline
@Entity(tableName = "symptoms")
value class SymptomEntity(val value: String)

fun SymptomEntity.toSymptom() = Symptom(name = value)

fun Symptom.toSymptomEntity() = SymptomEntity(value = name)
