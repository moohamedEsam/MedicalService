package com.example.medicalservice.presentation.diagnosis.form

import com.example.model.app.disease.Symptom

data class DiagnosisFormState(
    val description: String = "",
    val symptoms: List<Symptom> = emptyList(),
    val query: String = "",
    val selectedSymptoms: List<Symptom> = emptyList(),
    val isLoading: Boolean = false,
){
    val isDiagnosisButtonEnabled = selectedSymptoms.isNotEmpty() && description.isNotBlank() && !isLoading
}
