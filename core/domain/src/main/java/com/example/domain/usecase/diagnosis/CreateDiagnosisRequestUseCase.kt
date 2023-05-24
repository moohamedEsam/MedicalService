package com.example.domain.usecase.diagnosis

import com.example.common.models.Result
import com.example.model.app.diagnosis.DiagnosisRequest

fun interface CreateDiagnosisRequestUseCase : suspend (DiagnosisRequest) -> Result<DiagnosisRequest>