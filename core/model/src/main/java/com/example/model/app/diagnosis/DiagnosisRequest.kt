package com.example.model.app.diagnosis

import com.example.model.app.disease.Symptom

data class DiagnosisRequest(
    val symptoms: List<Symptom>,
    val description: String,
    val id: String
){
    companion object
}

fun DiagnosisRequest.Companion.empty() = DiagnosisRequest(
    symptoms = emptyList(),
    description = "",
    id = ""
)
