package com.example.data.transaction

import androidx.paging.PagingSource
import com.example.model.app.TransactionView
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(): PagingSource<Int, TransactionView>

    fun getTransaction(id: String): Flow<TransactionView>

    suspend fun syncTransactions(): Boolean
}