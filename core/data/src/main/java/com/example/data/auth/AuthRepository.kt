package com.example.data.auth

import com.example.common.models.Result
import com.example.model.app.auth.Credentials
import com.example.model.app.auth.Token
import com.example.model.app.user.CreateUserDto
import com.example.model.app.user.User

sealed interface AuthRepository{
    suspend fun login(credentials: Credentials): Result<Token>
    suspend fun register(createUserDto: CreateUserDto): Result<Unit>

    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}