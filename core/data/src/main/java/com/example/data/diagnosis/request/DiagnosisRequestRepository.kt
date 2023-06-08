package com.example.data.diagnosis.request

import com.example.model.app.diagnosis.DiagnosisRequest
import kotlinx.coroutines.flow.Flow
import com.example.common.models.Result

interface DiagnosisRequestRepository {

    fun getDiagnosisRequestsFlow(): Flow<List<DiagnosisRequest>>

    suspend fun insertDiagnosisRequest(diagnosisRequest: DiagnosisRequest): Result<DiagnosisRequest>

    suspend fun updateDiagnosisRequest(diagnosisRequest: DiagnosisRequest): Result<DiagnosisRequest>

    suspend fun syncDiagnosisRequest(): Boolean
}