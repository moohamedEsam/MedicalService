package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.database.models.TransactionEntity
import com.example.database.models.TransactionEntityView

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    suspend fun getTransactions(): DataSource.Factory<Int, TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionEntityView?

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE transactions SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("UPDATE transactions Set quantity = :quantity WHERE id = :id AND status = 'Pending'")
    suspend fun updateQuantity(id: String, quantity: Int)

    @Insert
    suspend fun insert(transaction: TransactionEntity)
}