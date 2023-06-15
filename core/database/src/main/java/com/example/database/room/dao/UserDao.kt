package com.example.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.database.models.user.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("select * from users where email = :email")
    fun getUserByEmail(email: String): Flow<UserEntity?>

    @Query("select id from users where email = :email")
    suspend fun getUserIdByEmail(email: String): String?

    @Insert
    suspend fun insert(user: UserEntity)

    @Insert
    suspend fun insertAll(users: List<UserEntity>)

    @Query("delete from users")
    suspend fun deleteAllUsers()
}