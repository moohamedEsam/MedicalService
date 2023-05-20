package com.example.domain.usecase.user

import com.example.model.app.user.User
import kotlinx.coroutines.flow.Flow

fun interface GetCurrentUserUseCase : () -> Flow<User>