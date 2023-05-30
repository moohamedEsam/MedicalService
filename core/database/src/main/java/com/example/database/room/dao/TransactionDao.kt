package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.models.transaction.TransactionEntity
import com.example.database.models.transaction.TransactionEntityView
import com.example.model.app.transaction.TransactionView
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    @Transaction
    fun getTransactions(): DataSource.Factory<Int, TransactionEntityView>

    @Query("SELECT * FROM transactions where receiverId !=:userId and senderId !=:userId and status =:status")
    fun getTransactions(
        userId: String,
        status: TransactionView.Status = TransactionView.Status.Active
    ): DataSource.Factory<Int, TransactionEntityView>

    @Query("SELECT * FROM transactions where receiverId =:userId or senderId =:userId limit 10")
    @Transaction
    fun getRecentTransactions(userId: String): Flow<List<TransactionEntityView>>

    @Query("SELECT * FROM transactions")
    @Transaction
    fun getTransactionsFlow(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE senderId = :userId or receiverId = :userId")
    @Transaction
    fun getTransactionsByUserId(userId: String): DataSource.Factory<Int, TransactionEntityView>

    @Query("SELECT * FROM transactions where isCreated = 1")
    suspend fun getCreatedTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions where isUpdated = 1 and isCreated = 0")
    suspend fun getUpdatedTransactions(): List<TransactionEntity>

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

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Insert
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Query("update transactions set isDeleted = :isDeleted where id = :id")
    suspend fun setTransactionDeleted(id: String, isDeleted: Boolean)
}