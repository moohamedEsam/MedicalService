package com.example.medicalservice.presentation.diagnosis.details

import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.diagnosis.empty

data class DiagnosisDetailsScreenState(
    val diagnosisResultView: DiagnosisResultView = DiagnosisResultView.empty(),
)