package com.example.domain.usecase.transaction

import com.example.model.app.transaction.TransactionView
import kotlinx.coroutines.flow.Flow

fun interface GetRecentTransactionsUseCase : () -> Flow<List<TransactionView>>