package com.example.domain.usecase

import androidx.paging.PagingSource

fun interface GetCurrentUserTransactionsUseCase : () -> PagingSource<Int, com.example.model.app.TransactionView>