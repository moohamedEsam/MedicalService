package com.example.medicalservice.domain

import com.example.models.app.Transaction

fun interface GetCurrentUserTransactionsUseCase : suspend () -> List<Transaction>