package com.example.medicalservice.presentation.diagnosis.details

import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.diagnosis.empty

sealed interface DiagnosisDetailsScreenEvent {
    object OnDoctorClick : DiagnosisDetailsScreenEvent
    data class OnMedicineClick(val medicineId: String) : DiagnosisDetailsScreenEvent
}