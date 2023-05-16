package com.example.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.database.models.user.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: UserEntity)

    @Insert
    suspend fun insertAll(users: List<UserEntity>)

    @Query("delete from users")
    suspend fun deleteAll()
}