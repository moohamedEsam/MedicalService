package com.example.domain.usecase.user

import com.example.common.models.Result
import com.example.model.app.user.CreateUserDto

fun interface RegisterUseCase : suspend (CreateUserDto) -> Result<Unit>