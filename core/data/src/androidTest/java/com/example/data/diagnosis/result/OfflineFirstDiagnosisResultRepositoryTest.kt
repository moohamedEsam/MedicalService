package com.example.data.diagnosis.result

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.database.room.MedicalServiceDatabase
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.empty
import com.example.network.KtorRemoteDataSource
import com.example.network.NetworkModule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OfflineFirstDiagnosisResultRepositoryTest {

    private lateinit var database: MedicalServiceDatabase
    private lateinit var diagnosisResultRepository: DiagnosisResultRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MedicalServiceDatabase::class.java
        ).allowMainThreadQueries().build()
        val remoteDataSource = KtorRemoteDataSource(NetworkModule.getTestClient())
        diagnosisResultRepository =
            OfflineFirstDiagnosisResultRepository(
                database.getDiagnosisResultDao(),
                remoteDataSource
            )
    }

    @Test
    fun syncDiagnosisResultWithInsert() = runTest {
        // Arrange
        val diagnosisResult = DiagnosisResult.empty()
        diagnosisResultRepository.insertDiagnosis(diagnosisResult)

        // Act
        val result = diagnosisResultRepository.syncDiagnosis()

        // Assert
        assertThat(result).isTrue()
        diagnosisResultRepository.getDiagnosisResults().test {
            val items = awaitItem()
            assertThat(items.map { it.id }).contains(diagnosisResult.id)
        }
    }

    @Test
    fun syncTransactionsWithUpdate() = runTest {
        // Arrange
        val diagnosisResult = DiagnosisResult.empty()
        diagnosisResultRepository.insertDiagnosis(diagnosisResult)

        // Act
        val result = diagnosisResultRepository.syncDiagnosis()
        assertThat(result).isTrue()

        diagnosisResultRepository.updateDiagnosis(diagnosisResult.copy(diagnosis = "new description"))
        val updatedResult = diagnosisResultRepository.syncDiagnosis()

        // Assert
        assertThat(updatedResult).isTrue()
        diagnosisResultRepository.getDiagnosisResults().test {
            val items = awaitItem()
            val updatedDiagnosisResult = items.find { it.id == diagnosisResult.id }
            assertThat(updatedDiagnosisResult?.diagnosis).isEqualTo("new description")
        }
    }
}