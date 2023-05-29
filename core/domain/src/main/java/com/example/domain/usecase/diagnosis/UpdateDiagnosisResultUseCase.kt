package com.example.domain.usecase.diagnosis

import com.example.common.models.Result
import com.example.model.app.diagnosis.DiagnosisResult

fun interface UpdateDiagnosisResultUseCase : suspend (DiagnosisResult) -> Result<DiagnosisResult>