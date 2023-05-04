package com.example.domain.usecase

import com.example.common.models.Result
import com.example.model.app.Credentials
import com.example.model.app.Token

fun interface LoginUseCase : suspend (Credentials) -> Result<Token>