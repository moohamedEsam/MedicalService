package com.example.domain.usecase.transaction

import com.example.common.models.Result

fun interface CreateTransactionUseCase: suspend (com.example.model.app.Transaction) -> Result<Unit>