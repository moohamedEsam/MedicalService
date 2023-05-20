package com.example.data.user

import com.example.common.functions.tryWrapper
import com.example.common.models.Result
import com.example.database.models.user.toUser
import com.example.database.models.user.toUserEntity
import com.example.database.room.dao.UserDao
import com.example.model.app.user.User
import com.example.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single([UserRepository::class])
class OfflineFirstUserRepository(
    private val local: UserDao,
    private val remote: RemoteDataSource
) : UserRepository {
    override fun getCurrentUser(email: String): Flow<User> =
        local.getUserByEmail(email).filterNotNull().map { it.toUser() }


    override suspend fun getCurrentUserId(email: String): String =
        local.getUserByEmail(email).filterNotNull().last().id

    override suspend fun syncUser(): Boolean {
        val usersResult = remote.getAllUsers()
        usersResult.ifSuccess { users ->
            local.deleteAll()
            local.insertAll(users.map { it.toUserEntity() })
        }
        return true
    }
}