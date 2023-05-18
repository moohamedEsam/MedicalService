package com.example.data.diagnosis.request

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.data.transaction.OfflineFirstTransactionRepository
import com.example.data.transaction.TransactionRepository
import com.example.database.room.MedicalServiceDatabase
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.empty
import com.example.model.app.transaction.Transaction
import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.empty
import com.example.network.KtorRemoteDataSource
import com.example.network.NetworkModule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OfflineFirstDiagnosisRequestRepositoryTest {

    private lateinit var database: MedicalServiceDatabase
    private lateinit var diagnosisRequestRepository: DiagnosisRequestRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MedicalServiceDatabase::class.java
        ).allowMainThreadQueries().build()
        val remoteDataSource = KtorRemoteDataSource(NetworkModule.getTestClient())
        diagnosisRequestRepository =
            OfflineFirstDiagnosisRequestRepository(
                database.getDiagnosisRequestDao(),
                remoteDataSource
            )
    }

    @Test
    fun syncDiagnosisRequestWithInsert() = runTest {
        // Arrange
        val diagnosisRequest = DiagnosisRequest.empty()
        diagnosisRequestRepository.insertDiagnosisRequest(diagnosisRequest)

        // Act
        val result = diagnosisRequestRepository.syncDiagnosisRequest()

        // Assert
        assertThat(result).isTrue()
        diagnosisRequestRepository.getDiagnosisRequestsFlow().test {
            val items = awaitItem()
            assertThat(items.map { it.id }).contains(diagnosisRequest.id)
        }
    }

    @Test
    fun syncTransactionsWithUpdate() = runTest {
        // Arrange
        val diagnosisRequest = DiagnosisRequest.empty()
        diagnosisRequestRepository.insertDiagnosisRequest(diagnosisRequest)

        // Act
        val result = diagnosisRequestRepository.syncDiagnosisRequest()
        assertThat(result).isTrue()

        diagnosisRequestRepository.updateDiagnosisRequest(diagnosisRequest.copy(description = "new description"))
        val updatedResult = diagnosisRequestRepository.syncDiagnosisRequest()

        // Assert
        assertThat(updatedResult).isTrue()
        diagnosisRequestRepository.getDiagnosisRequestsFlow().test {
            val items = awaitItem()
            val updatedDiagnosisRequest = items.find { it.id == diagnosisRequest.id }
            assertThat(updatedDiagnosisRequest?.description).isEqualTo("new description")
        }
    }
}