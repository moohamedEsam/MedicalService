package com.example.domain.usecase.user

import com.example.common.models.Result
import com.example.model.app.Register

fun interface RegisterUseCase : suspend (Register) -> Result<Unit>