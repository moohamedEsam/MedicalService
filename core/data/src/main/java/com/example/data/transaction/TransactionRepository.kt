package com.example.data.transaction

import androidx.paging.PagingSource
import com.example.common.models.Result
import com.example.model.app.transaction.Transaction
import com.example.model.app.transaction.TransactionView
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(userId: String): () -> PagingSource<Int, TransactionView>

    fun getRecentTransactions(userId: String) : Flow<List<TransactionView>>

    fun getTransactionsFlow(): Flow<List<Transaction>>

    fun getTransactionsByUserId(userId: String): () -> PagingSource<Int, TransactionView>
    fun getTransaction(id: String): Flow<TransactionView>

    suspend fun insertTransaction(transaction: Transaction) : Result<Unit>

    suspend fun updateTransaction(transaction: Transaction) : Result<Unit>

    suspend fun deleteTransaction(id: String) : Result<Unit>
    suspend fun syncTransactions(): Boolean
}