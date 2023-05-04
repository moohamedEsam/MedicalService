package com.example.data

import com.example.common.models.Result
import com.example.model.app.Credentials
import com.example.model.app.Register
import com.example.model.app.Token
import com.example.network.RemoteDataSource
import org.koin.core.annotation.Single

@Single([AuthRepository::class])
class KtorAuthRepository(
    private val remoteDataSource: RemoteDataSource
) : AuthRepository {
    override suspend fun login(credentials: Credentials): Result<Token> =
        remoteDataSource.login(credentials)

    override suspend fun register(register: Register): Result<Unit> =
        remoteDataSource.register(register)

    override suspend fun logout() = Unit

    override suspend fun isLoggedIn(): Boolean = true
}