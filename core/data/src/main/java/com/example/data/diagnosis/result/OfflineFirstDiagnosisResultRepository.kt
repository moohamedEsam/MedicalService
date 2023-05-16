package com.example.data.diagnosis.result

import com.example.common.functions.tryWrapper
import com.example.common.models.Result
import com.example.database.models.diagnosis.toDiagnosisResult
import com.example.database.models.diagnosis.toDiagnosisResultView
import com.example.database.models.diagnosis.toEntity
import com.example.database.room.dao.DiagnosisResultDao
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single([DiagnosisResultRepository::class])
class OfflineFirstDiagnosisResultRepository(
    private val local: DiagnosisResultDao,
    private val remote: RemoteDataSource
) : DiagnosisResultRepository {
    override suspend fun insertDiagnosis(diagnosis: DiagnosisResult): Result<DiagnosisResult> =
        tryWrapper {
            local.insert(diagnosis.toEntity().copy(isCreated = true))
            Result.Success(diagnosis)
        }

    override suspend fun updateDiagnosis(diagnosis: DiagnosisResult): Result<DiagnosisResult> =
        tryWrapper {
            local.insert(diagnosis.toEntity().copy(isUpdated = true))
            Result.Success(diagnosis)
        }

    override fun getDiagnosis(id: String): Flow<DiagnosisResultView> = local
        .getDiagnosisResult(id).filterNotNull().map { it.toDiagnosisResultView() }

    override suspend fun deleteDiagnosis(id: String): Result<Unit> = tryWrapper {
        local.deleteDiagnosisResult(id)
        Result.Success(Unit)
    }

    override suspend fun syncDiagnosis(): Boolean {
        val createdDiagnosisResults = local.getCreatedDiagnosisResults()
        createdDiagnosisResults.forEach { remote.createDiagnosisResult(it.toDiagnosisResult()) }
        val remoteDiagnosisResults = remote.getCurrentUserDiagnosisResults()
        remoteDiagnosisResults.ifSuccess {
            local.deleteAllDiagnosisResults()
            local.insertAll(it.map { diagnosisResult -> diagnosisResult.toEntity() })
        }
        return remoteDiagnosisResults is Result.Success
    }
}