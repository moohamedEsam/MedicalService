package com.example.data.user

import com.example.common.models.Result
import com.example.model.app.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getCurrentUser(email: String): Flow<User>

    suspend fun getCurrentUserId(email: String): String?
    suspend fun syncUser(): Boolean
}