package com.example.domain.usecase.transaction

import androidx.paging.PagingSource

fun interface GetCurrentUserTransactionsUseCase : () -> PagingSource<Int, com.example.model.app.TransactionView>