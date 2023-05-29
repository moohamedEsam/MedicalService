package com.example.data.disease

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.database.models.disease.DiseaseMedicineCrossRef
import com.example.database.models.disease.toEntity
import com.example.database.models.medicine.toEntity
import com.example.database.room.MedicalServiceDatabase
import com.example.model.app.disease.Disease
import com.example.model.app.disease.empty
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import com.example.network.KtorRemoteDataSource
import com.example.network.NetworkModule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiseaseRepositoryTest {
    private lateinit var database: MedicalServiceDatabase
    private lateinit var diseaseRepository: DiseaseRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MedicalServiceDatabase::class.java
        ).allowMainThreadQueries().build()
        diseaseRepository = OfflineFirstDiseaseRepository(
            database.getDiseaseDao(),
            KtorRemoteDataSource(NetworkModule.getTestClient())
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun syncDiseases() = runTest {
        // Arrange
        val disease = Disease.empty().copy(name = "headache")
        val medicine = Medicine.empty().copy(name = "aspirin").toEntity()
        diseaseRepository.insertDisease(disease)
        database.getMedicineDao().insertAll(listOf(medicine), listOf(DiseaseMedicineCrossRef(disease.id, medicine.id)))

        // Act
        diseaseRepository.getDiseaseDetails(disease.id).test {
            val item = awaitItem()
            assertThat(item.medicines).isNotEmpty()
        }
        val result = diseaseRepository.syncDiseases()
        assertThat(result).isTrue()
        diseaseRepository.getDiseasesFlow().test {
            val item = awaitItem()
            assertThat(item).isNotEmpty()
            assertThat(item.size).isEqualTo(1)
            assertThat(item.firstOrNull()?.medicines).isNotEmpty()
        }

    }
}