package com.example.domain.usecase.transaction

import com.example.common.models.Result
import com.example.model.app.transaction.Transaction

fun interface DeleteTransactionUseCase: suspend (String) -> Result<Unit>