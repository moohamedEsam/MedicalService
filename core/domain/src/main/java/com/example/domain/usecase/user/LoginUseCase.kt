package com.example.domain.usecase.user

import com.example.common.models.Result
import com.example.model.app.auth.Credentials
import com.example.model.app.auth.Token

fun interface LoginUseCase : suspend (Credentials) -> Result<Token>