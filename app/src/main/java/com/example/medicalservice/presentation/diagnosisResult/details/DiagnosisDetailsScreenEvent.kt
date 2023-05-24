package com.example.medicalservice.presentation.diagnosisResult.details

sealed interface DiagnosisDetailsScreenEvent {
    object OnDoctorClick : DiagnosisDetailsScreenEvent
    data class OnMedicineClick(val medicineId: String) : DiagnosisDetailsScreenEvent
}