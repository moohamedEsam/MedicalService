package com.example.data.user

import com.example.database.models.user.toUserEntity
import com.example.database.room.dao.UserDao
import com.example.network.RemoteDataSource
import org.koin.core.annotation.Single

@Single([UserRepository::class])
class OfflineFirstUserRepository(
    private val local: UserDao,
    private val remote: RemoteDataSource
) : UserRepository {
    override suspend fun syncUser(): Boolean {
        val usersResult = remote.getAllUsers()
        usersResult.ifSuccess { users ->
            local.deleteAll()
            local.insertAll(users.map { it.toUserEntity() })
        }
        return true
    }
}