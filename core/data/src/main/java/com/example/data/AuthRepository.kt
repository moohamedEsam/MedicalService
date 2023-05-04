package com.example.data

import com.example.common.models.Result
import com.example.model.app.Credentials
import com.example.model.app.Register
import com.example.model.app.Token

sealed interface AuthRepository{
    suspend fun login(credentials: Credentials): Result<Token>
    suspend fun register(register: Register): Result<Unit>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}