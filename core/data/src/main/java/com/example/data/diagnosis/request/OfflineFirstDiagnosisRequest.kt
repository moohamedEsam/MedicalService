package com.example.data.diagnosis.request

import com.example.common.functions.tryWrapper
import com.example.database.models.diagnosis.toEntity
import com.example.database.room.dao.DiagnosisRequestDao
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.network.RemoteDataSource
import org.koin.core.annotation.Single
import com.example.common.models.Result
import com.example.database.models.diagnosis.toDiagnosisRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@Single([DiagnosisRequestRepository::class])
class OfflineFirstDiagnosisRequestRepository(
    private val local: DiagnosisRequestDao,
    private val remote: RemoteDataSource
) : DiagnosisRequestRepository {
    override suspend fun insertDiagnosisRequest(diagnosisRequest: DiagnosisRequest): Result<DiagnosisRequest> =
        tryWrapper {
            local.insert(diagnosisRequest.toEntity().copy(isCreated = true))
            Result.Success(diagnosisRequest)
        }

    override suspend fun updateDiagnosisRequest(diagnosisRequest: DiagnosisRequest): Result<DiagnosisRequest> =
        tryWrapper {
            local.insert(diagnosisRequest.toEntity().copy(isUpdated = true))
            Result.Success(diagnosisRequest)
        }

    override fun getDiagnosisRequest(id: String): Flow<DiagnosisRequest> = local
        .getDiagnosisRequest(id).filterNotNull().map { it.toDiagnosisRequest() }

    override fun getLatestDiagnosisRequest(): Flow<DiagnosisRequest> = local
        .getLatestDiagnosisRequest().filterNotNull().map { it.toDiagnosisRequest() }

    override suspend fun deleteDiagnosisRequest(id: String): Result<Unit> = tryWrapper {
        local.deleteDiagnosisRequest(id)
        Result.Success(Unit)
    }

    override suspend fun syncDiagnosisRequest(): Boolean {
        val createdDiagnosisRequests = local.getCreatedDiagnosisRequests()
        createdDiagnosisRequests.forEach { remote.createDiagnosisRequest(it.toDiagnosisRequest()) }
        val remoteDiagnosisRequests = remote.getCurrentUserDiagnosisRequests()
        remoteDiagnosisRequests.ifSuccess {
            local.deleteAllDiagnosisRequests()
            local.insertAll(it.map { diagnosisRequest -> diagnosisRequest.toEntity() })
        }
        return remoteDiagnosisRequests is Result.Success
    }
}