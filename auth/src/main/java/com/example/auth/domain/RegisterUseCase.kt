package com.example.auth.domain

import com.example.common.models.Result
import com.example.models.auth.Register
import com.example.models.auth.Token

fun interface RegisterUseCase : suspend (Register) -> Result<Unit>