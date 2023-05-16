package com.example.data.user

interface UserRepository {
    suspend fun syncUser(): Boolean
}