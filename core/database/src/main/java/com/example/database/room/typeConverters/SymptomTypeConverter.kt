package com.example.database.room.typeConverters

import androidx.room.TypeConverter
import com.example.model.app.disease.Symptom
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SymptomTypeConverter {
    @TypeConverter
    fun fromSymptomList(symptoms: List<Symptom>): String = Json.encodeToString(symptoms)

    @TypeConverter
    fun toSymptomList(symptoms: String): List<Symptom> = Json.decodeFromString(symptoms)
}