package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.models.transaction.TransactionEntity
import com.example.database.models.transaction.TransactionEntityView
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    @Transaction
    fun getTransactions(): DataSource.Factory<Int, TransactionEntityView>

    @Query("SELECT * FROM transactions WHERE id = :id")
    @Transaction
    fun getTransactionById(id: String): Flow<TransactionEntityView?>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("UPDATE transactions SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("UPDATE transactions Set quantity = :quantity WHERE id = :id AND status = 'Pending'")
    suspend fun updateQuantity(id: String, quantity: Int)

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Insert
    suspend fun insertAll(transactions: List<TransactionEntity>)
}