package com.example.auth.domain

import com.example.common.models.Result
import com.example.models.auth.Register

fun interface RegisterUseCase : (Register) -> Result<Unit>