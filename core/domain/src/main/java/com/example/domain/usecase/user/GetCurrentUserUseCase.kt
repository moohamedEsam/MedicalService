package com.example.domain.usecase.user

import com.example.common.models.Result
import com.example.model.app.user.User

fun interface GetCurrentUserUseCase : suspend () -> Result<User>