package com.example.data.transaction

import androidx.paging.PagingSource
import com.example.common.functions.tryWrapper
import com.example.common.models.Result
import com.example.database.models.transaction.TransactionEntityView
import com.example.database.models.transaction.toEntity
import com.example.database.models.transaction.toTransactionView
import com.example.database.room.dao.TransactionDao
import com.example.model.app.Transaction
import com.example.model.app.TransactionView
import com.example.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single([TransactionRepository::class])
class OfflineFirstTransactionRepository(
    private val local: TransactionDao,
    private val remote: RemoteDataSource
) : TransactionRepository {
    override fun getTransactions(): PagingSource<Int, TransactionView> =
        local.getTransactions().map { it.toTransactionView() }.asPagingSourceFactory().invoke()

    override fun getTransaction(id: String): Flow<TransactionView> =
        local.getTransactionById(id).filterNotNull().map(TransactionEntityView::toTransactionView)

    override suspend fun insertTransaction(transaction: Transaction): Result<Unit> = tryWrapper {
        local.insert(transaction.toEntity())
        Result.Success(Unit)
    }

    override suspend fun syncTransactions(): Boolean {
        val transactions = remote.getUserTransactions()
        transactions.ifSuccess {
            local.deleteAll()
            local.insertAll(it.map { transaction -> transaction.toEntity() })
        }
        return transactions is Result.Success
    }
}