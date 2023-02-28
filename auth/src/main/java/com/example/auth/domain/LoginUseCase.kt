package com.example.auth.domain

import com.example.common.models.Result
import com.example.models.auth.Credentials

fun interface LoginUseCase: (Credentials) -> Result<Unit>