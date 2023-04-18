package com.example.models

import com.example.common.models.Result
import com.example.models.auth.Credentials
import com.example.models.auth.Token

interface RemoteDataSource {
    suspend fun login(credentials: Credentials): Result<Token>
}