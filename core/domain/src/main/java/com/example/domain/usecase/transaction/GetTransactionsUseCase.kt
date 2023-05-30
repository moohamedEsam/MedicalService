package com.example.domain.usecase.transaction

import androidx.paging.PagingSource
import com.example.model.app.transaction.TransactionView

fun interface GetTransactionsUseCase : (String) -> () -> PagingSource<Int, TransactionView>