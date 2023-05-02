package com.example.auth

import com.example.auth.domain.LoginUseCase
import com.example.auth.domain.RegisterUseCase
import com.example.common.models.Result
import com.example.models.RemoteDataSource
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class AuthModule{
    @Factory
    fun registerUseCase(repository: RemoteDataSource) = RegisterUseCase(repository::register)

    @Factory
    fun loginUseCase(repository: RemoteDataSource) = LoginUseCase(repository::login)
}