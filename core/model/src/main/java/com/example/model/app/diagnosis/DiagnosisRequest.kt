package com.example.model.app.diagnosis

import com.example.model.app.disease.Symptom
import java.util.Date
import java.util.UUID

data class DiagnosisRequest(
    val symptoms: List<Symptom>,
    val description: String,
    val id: String = UUID.randomUUID().toString(),
    val date: Date = Date()
){
    companion object
}

fun DiagnosisRequest.Companion.empty() = DiagnosisRequest(
    symptoms = emptyList(),
    description = "",
    id = ""
)
