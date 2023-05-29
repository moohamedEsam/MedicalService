package com.example.data.medicine


import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.data.disease.DiseaseRepository
import com.example.data.disease.OfflineFirstDiseaseRepository
import com.example.database.room.MedicalServiceDatabase
import com.example.model.app.disease.Disease
import com.example.model.app.disease.empty
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import com.example.network.KtorRemoteDataSource
import com.example.network.NetworkModule
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineRepositoryTest {
    private lateinit var database: MedicalServiceDatabase
    private lateinit var medicineRepository: MedicineRepository
    private lateinit var diseaseRepository: DiseaseRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MedicalServiceDatabase::class.java
        ).allowMainThreadQueries().build()
        medicineRepository = OfflineFirstMedicineRepository(
            database.getMedicineDao(),
            KtorRemoteDataSource(NetworkModule.getTestClient())
        )
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
    fun syncMedicines() = runTest {
        // Arrange
        val disease = Disease.empty().copy(name = "headache")
        val medicine = Medicine.empty().copy(name = "aspirin", diseasesId = listOf(disease.id))
        diseaseRepository.insertDisease(disease)
        medicineRepository.createMedicine(medicine)

        // Act
        assertThat(diseaseRepository.syncDiseases()).isTrue()
        medicineRepository.getMedicineDetails(medicine.id).test {
            val item = awaitItem()
            assertThat(item.diseases).isNotEmpty()
        }
        val result = medicineRepository.syncMedicines()
        assertThat(result).isTrue()
        medicineRepository.getMedicinesFlow().test {
            val item = awaitItem()
            assertThat(item).isNotEmpty()
            assertThat(item.size).isEqualTo(1)
            assertThat(item.firstOrNull()?.diseases).isNotEmpty()
        }

    }
}