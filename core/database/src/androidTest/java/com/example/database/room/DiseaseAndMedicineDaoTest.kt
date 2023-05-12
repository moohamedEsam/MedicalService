package com.example.database.room

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.database.models.disease.DiseaseMedicineCrossRef
import com.example.database.models.disease.toEntity
import com.example.database.models.medicine.toEntity
import com.example.database.room.dao.DiseaseDao
import com.example.database.room.dao.MedicineDao
import com.example.model.app.disease.Disease
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiseaseAndMedicineDaoTest {
    private lateinit var database: MedicalServiceDatabase

    @OptIn(DelicateCoroutinesApi::class)
    private val thread = newSingleThreadContext("test")
    private lateinit var diseaseDao: DiseaseDao
    private lateinit var medicineDao: MedicineDao

    @Before
    fun setUp() {
        Dispatchers.setMain(thread)
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = MedicalServiceDatabase::class.java
        ).build()
        diseaseDao = database.getDiseaseDao()
        medicineDao = database.getMedicineDao()
    }

    @Test
    fun testGetDiseases() = runTest {
        val disease = Disease.empty().toEntity()
        diseaseDao.insert(disease)
        val pagingSource = diseaseDao.getDiseases()
            .asPagingSourceFactory().invoke()
        val actual = PagingSource.LoadResult
            .Page(listOf(disease), null, null, 0, 0)

        assertThat(
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    null,
                    1,
                    false
                )
            )
        ).isEqualTo(actual)
    }

    @Test
    fun testGetDisease() = runTest {
        val disease = Disease.empty().toEntity()
        diseaseDao.insert(disease)
        diseaseDao.getDisease(disease.id).test {
            val item = awaitItem()
            assertThat(item).isNotNull()
            assertThat(item?.disease).isEqualTo(disease)
        }
    }

    @Test
    fun testGetDiseaseWithMedicines() = runTest {
        val disease = Disease.empty().toEntity()
        val medicine = Medicine.empty().toEntity()
        medicineDao.insertAll(listOf(medicine))
        diseaseDao.insertAll(listOf(disease), listOf(DiseaseMedicineCrossRef(disease.id, medicine.id)))
        diseaseDao.getDisease(disease.id).test {
            val item = awaitItem()
            assertThat(item).isNotNull()
            assertThat(item?.disease).isEqualTo(disease)
            assertThat(item?.medicines).containsExactly(medicine)
        }
    }

    @Test
    fun testGetMedicinesWithDiseases() = runTest {
        val disease = Disease.empty().toEntity()
        val medicine = Medicine.empty().toEntity()
        medicineDao.insertAll(listOf(medicine), listOf(DiseaseMedicineCrossRef(disease.id, medicine.id)))
        diseaseDao.insertAll(listOf(disease))
        medicineDao.getMedicine(medicine.id).test {
            val item = awaitItem()
            assertThat(item).isNotNull()
            assertThat(item?.medicineEntity).isEqualTo(medicine)
            assertThat(item?.diseases).containsExactly(disease)
        }
    }

    @After
    fun tearDown() {
        database.close()
        thread.close()
    }
}