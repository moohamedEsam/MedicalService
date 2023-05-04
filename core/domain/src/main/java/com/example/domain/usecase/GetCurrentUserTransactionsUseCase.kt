package com.example.domain.usecase

fun interface GetCurrentUserTransactionsUseCase : suspend () -> List<com.example.model.app.TransactionView>