package com.example.data.transaction

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.database.room.MedicalServiceDatabase
import com.example.model.app.transaction.Transaction
import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.empty
import com.example.network.KtorRemoteDataSource
import com.example.network.NetworkModule
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
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionRepositoryTest {
    private lateinit var database: MedicalServiceDatabase
    private lateinit var transactionRepository: TransactionRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MedicalServiceDatabase::class.java
        ).allowMainThreadQueries().build()
        val remoteDataSource = KtorRemoteDataSource(NetworkModule.getTestClient())
        transactionRepository =
            OfflineFirstTransactionRepository(database.getTransactionDao(), remoteDataSource)
    }

    @Test
    fun syncTransactionsWithInsert() = runTest {
        // Arrange
        val transaction = Transaction.empty()
        transactionRepository.insertTransaction(transaction)

        // Act
        val result = transactionRepository.syncTransactions()

        // Assert
        assertThat(result).isTrue()
        transactionRepository.getTransactionsFlow().test {
            val items = awaitItem()
            assertThat(items.map { it.id }).contains(transaction.id)
        }

    }

    @Test
    fun syncTransactionsWithUpdate() = runTest {
        // Arrange
        val transaction = Transaction.empty()
        transactionRepository.insertTransaction(transaction)

        // Act
        val result = transactionRepository.syncTransactions()
        // Assert
        assertThat(result).isTrue()

        // Act
        transactionRepository.updateTransaction(transaction.copy(status = TransactionView.Status.Completed))
        val updateResult = transactionRepository.syncTransactions()

        // Assert
        assertThat(updateResult).isTrue()
        transactionRepository.getTransactionsFlow().test {
            val items = awaitItem()
            val updatedTransaction = items.find { it.id == transaction.id }
            assertThat(updatedTransaction).isNotNull()
            assertThat(updatedTransaction!!.status).isEqualTo(TransactionView.Status.Completed)
        }

    }

    @After
    fun tearDown() {
        database.close()
    }
}