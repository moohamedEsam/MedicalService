package com.example.domain.usecase.transaction

import androidx.paging.PagingSource
import com.example.model.app.transaction.TransactionView

fun interface GetCurrentUserTransactionsUseCase : () -> PagingSource<Int, TransactionView>