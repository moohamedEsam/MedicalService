package com.example.data.auth

import com.example.common.models.Result
import com.example.database.room.ClearDatabaseUseCase
import com.example.database.room.dao.UserDao
import com.example.model.app.auth.Credentials
import com.example.model.app.auth.Token
import com.example.model.app.user.CreateUserDto
import com.example.model.app.user.User
import com.example.network.RemoteDataSource
import org.koin.core.annotation.Single

@Single([AuthRepository::class])
class KtorAuthRepository(
    private val remoteDataSource: RemoteDataSource,
    private val clearDatabaseUseCase: ClearDatabaseUseCase
) : AuthRepository {
    override suspend fun login(credentials: Credentials): Result<Token> =
        remoteDataSource.login(credentials)

    override suspend fun register(createUserDto: CreateUserDto): Result<Unit> =
        remoteDataSource.register(createUserDto)

    override suspend fun logout() {
        clearDatabaseUseCase()
        remoteDataSource.logout()
    }

}