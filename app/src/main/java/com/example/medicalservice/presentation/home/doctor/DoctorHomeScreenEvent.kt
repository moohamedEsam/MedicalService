package com.example.medicalservice.presentation.home.doctor

sealed interface DoctorHomeScreenEvent {
    data class DiagnosisResultClicked(val diagnosisResultId: String) : DoctorHomeScreenEvent
}