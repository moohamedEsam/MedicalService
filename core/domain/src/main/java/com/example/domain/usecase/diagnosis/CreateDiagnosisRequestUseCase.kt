package com.example.domain.usecase.diagnosis

import com.example.common.models.Result
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.DiagnosisResultView
import kotlinx.coroutines.flow.Flow

fun interface CreateDiagnosisRequestUseCase : (DiagnosisRequest) -> Result<DiagnosisRequest>