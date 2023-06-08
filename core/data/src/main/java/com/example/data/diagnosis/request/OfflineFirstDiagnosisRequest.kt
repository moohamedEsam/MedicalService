package com.example.data.diagnosis.request

import com.example.common.functions.tryWrapper
import com.example.common.models.Result
import com.example.database.models.diagnosis.toDiagnosisRequest
import com.example.database.models.diagnosis.toEntity
import com.example.database.room.dao.DiagnosisRequestDao
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

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
            local.update(diagnosisRequest.toEntity().copy(isUpdated = true))
            Result.Success(diagnosisRequest)
        }

    override suspend fun syncDiagnosisRequest(): Boolean {
        val createdDiagnosisRequests = local.getCreatedDiagnosisRequests()
        createdDiagnosisRequests.forEach { remote.createDiagnosisRequest(it.toDiagnosisRequest()) }

        val updatedDiagnosisRequests = local.getUpdatedDiagnosisRequests()
        updatedDiagnosisRequests.forEach { remote.updateDiagnosisRequest(it.toDiagnosisRequest()) }

        val remoteDiagnosisRequests = remote.getCurrentUserDiagnosisRequests()
        remoteDiagnosisRequests.ifSuccess {
            local.deleteAllDiagnosisRequests()
            local.insertAll(it.map { diagnosisRequest -> diagnosisRequest.toEntity() })
        }
        return remoteDiagnosisRequests is Result.Success
    }

    override fun getDiagnosisRequestsFlow(): Flow<List<DiagnosisRequest>> = local
        .getDiagnosisRequestsFlow().map { it.map { diagnosisRequest -> diagnosisRequest.toDiagnosisRequest() } }
}