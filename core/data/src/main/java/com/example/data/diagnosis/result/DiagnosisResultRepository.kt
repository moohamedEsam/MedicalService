package com.example.data.diagnosis.result

import androidx.paging.PagingSource
import com.example.common.models.Result
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.DiagnosisResultView
import kotlinx.coroutines.flow.Flow

interface DiagnosisResultRepository {

    suspend fun insertDiagnosis(diagnosis: DiagnosisResult): Result<DiagnosisResult>

    fun getDiagnosisResults(): Flow<List<DiagnosisResult>>

    fun getLatestDiagnosisResult(): Flow<DiagnosisResultView>

    fun getDiagnosisResultsView(): () -> PagingSource<Int, DiagnosisResultView>
    suspend fun updateDiagnosis(diagnosis: DiagnosisResult): Result<DiagnosisResult>

    fun getDiagnosis(id: String): Flow<DiagnosisResultView>

    suspend fun deleteDiagnosis(id: String) : Result<Unit>

    suspend fun syncDiagnosis(): Boolean

}