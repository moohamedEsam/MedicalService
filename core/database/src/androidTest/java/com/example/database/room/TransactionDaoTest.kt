package com.example.database.room

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.database.models.medicine.toEntity
import com.example.database.models.transaction.TransactionEntity
import com.example.database.models.transaction.TransactionEntityView
import com.example.database.models.transaction.toEntity
import com.example.database.models.user.toUserEntity
import com.example.database.room.dao.DonationRequestDao
import com.example.database.room.dao.MedicineDao
import com.example.database.room.dao.TransactionDao
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import com.example.model.app.transaction.Transaction
import com.example.model.app.transaction.empty
import com.example.model.app.user.User
import com.example.model.app.user.emptyDonor
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDaoTest {
    private lateinit var database: MedicalServiceDatabase

    @OptIn(DelicateCoroutinesApi::class)
    private val thread = newSingleThreadContext("test")
    private val medicine = Medicine.empty().toEntity()
    private val user = User.emptyDonor().toUserEntity()
    private lateinit var transactionDao: TransactionDao

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(thread)
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = MedicalServiceDatabase::class.java
        ).build()
        transactionDao = database.getTransactionDao()
        database.getMedicineDao().insertAll(listOf(medicine))
        database.getUserDao().insert(user)
    }

    @Test
    fun testGetTransactions() = runTest {
        // arrange
        val transaction =
            Transaction.empty().copy(medicineId = medicine.id, senderId = user.id).toEntity()

        // act
        transactionDao.insert(transaction)
        transactionDao.getTransactionsFlow().test {
            val item = awaitItem()
            // assert
            assertThat(item).isNotEmpty()
            val actual = item.find { it.id == transaction.id }
            assertThat(actual).isNotNull()
        }
    }

    @Test
    fun testGetTransactionsByUserId() = runTest {
        // arrange
        val transaction =
            Transaction.empty().copy(medicineId = medicine.id, senderId = user.id).toEntity()

        // act
        transactionDao.insert(transaction)
        transactionDao.insert(transaction.copy(id = "random", senderId = "random"))

        val dataSource =
            transactionDao.getTransactionsByUserId(user.id)
                .map { it.transactionEntity }
                .asPagingSourceFactory()
                .invoke()

        // assert
        val load = dataSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        ) as? PagingSource.LoadResult.Page
        assertThat(load?.data).isEqualTo(listOf(transaction))
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
        thread.close()
    }
}