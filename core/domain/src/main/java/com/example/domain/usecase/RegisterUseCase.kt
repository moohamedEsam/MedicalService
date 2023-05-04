package com.example.domain.usecase

import com.example.common.models.Result
import com.example.model.app.Register

fun interface RegisterUseCase : suspend (Register) -> Result<Unit>