package com.example.medicalservice.presentation.diagnosis.form

import com.example.model.app.disease.Symptom

sealed interface DiagnosisFormEvent {
    object OnSubmitClick : DiagnosisFormEvent
    data class OnSymptomClick(val symptom: Symptom) : DiagnosisFormEvent
    data class OnQueryChange(val query: String) : DiagnosisFormEvent
    data class OnDescriptionChange(val description: String) : DiagnosisFormEvent
    data class OnImagePicked(val imageUri: String) : DiagnosisFormEvent
}