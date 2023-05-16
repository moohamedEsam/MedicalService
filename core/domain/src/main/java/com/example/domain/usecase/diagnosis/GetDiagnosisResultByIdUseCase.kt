package com.example.domain.usecase.diagnosis

import com.example.model.app.diagnosis.DiagnosisResultView
import kotlinx.coroutines.flow.Flow

fun interface GetDiagnosisResultByIdUseCase : suspend (String) -> Flow<DiagnosisResultView>