package com.example.database.room

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.database.models.donation.toEntity
import com.example.database.models.medicine.toEntity
import com.example.database.room.dao.DonationRequestDao
import com.example.database.room.dao.MedicineDao
import com.example.model.app.donation.DonationRequest
import com.example.model.app.disease.empty
import com.example.model.app.donation.empty
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
class DonationRequestDaoTest {
    private lateinit var database: MedicalServiceDatabase

    @OptIn(DelicateCoroutinesApi::class)
    private val thread = newSingleThreadContext("test")
    private lateinit var donationRequestDao: DonationRequestDao
    private lateinit var medicineDao: MedicineDao
    private val medicine = Medicine.empty().toEntity()

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(thread)
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = MedicalServiceDatabase::class.java
        ).build()
        medicineDao = database.getMedicineDao()
        medicineDao.insertAll(listOf(medicine))
        donationRequestDao = database.getDonationRequestDao()
    }

    @Test
    fun testGetDonationRequests() = runTest {
        val donationRequest = DonationRequest.empty().copy(medicineId = medicine.id).toEntity()
        donationRequestDao.insert(donationRequest)
        val actual = PagingSource.LoadResult.Page(
            data = listOf(donationRequest),
            prevKey = null,
            nextKey = null,
            itemsBefore = 0,
            itemsAfter = 0
        )

        val pagingSource = donationRequestDao.getDonationRequests()
            .map { it.donationRequestEntity }
            .asPagingSourceFactory().invoke()

        assertThat(
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        ).isEqualTo(actual)
    }

    @Test
    fun testGetDonationRequestsById() = runTest {
        val donationRequest = DonationRequest.empty().copy(medicineId = medicine.id).toEntity()
        donationRequestDao.insert(donationRequest)
        donationRequestDao.getDonationRequestById(donationRequest.id).test {
            val item = awaitItem()
            assertThat(item?.donationRequestEntity).isEqualTo(donationRequest)
            assertThat(item?.medicine?.medicineEntity).isEqualTo(medicine)
        }
    }

    @After
    fun tearDown() {
        database.close()
        thread.close()
    }
}