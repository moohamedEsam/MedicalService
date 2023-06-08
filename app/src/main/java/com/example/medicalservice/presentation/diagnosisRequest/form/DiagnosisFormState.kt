package com.example.medicalservice.presentation.diagnosisRequest.form

import com.example.model.app.disease.Symptom

data class DiagnosisFormState(
    val description: String = "",
    val symptoms: List<Symptom> = emptyList(),
    val query: String = "",
    val selectedSymptoms: List<Symptom> = emptyList(),
    val isLoading: Boolean = false,
){
    val isDiagnosisButtonEnabled = selectedSymptoms.isNotEmpty() && description.isNotBlank() && !isLoading

    val filteredSymptoms: List<Symptom>
        get() = if (query.isBlank()) symptoms else symptoms.filter { it.name.contains(query, ignoreCase = true) }
}
