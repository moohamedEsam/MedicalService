package com.example.medicalservice.presentation.home.doctor

import androidx.paging.PagingData
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.DiagnosisResultView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class DoctorHomeScreenState(
    val diagnosisResults: Flow<PagingData<DiagnosisResultView>> = emptyFlow()
)