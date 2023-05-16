package com.example.database.models.user

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.app.user.Location
import com.example.model.app.user.User
import com.example.model.app.user.UserType

@Entity(tableName = "users")
data class UserEntity(
    val username: String,
    val email: String,
    val phone: String,
    @Embedded val location: Location,
    val type: UserType,
    @PrimaryKey val id: String
)

fun UserEntity.toUser() = when (type) {
    UserType.Donner -> User.Donor(
        id = id,
        username = username,
        email = email,
        phone = phone,
        location = location,
    )

    UserType.Receiver -> User.Receiver(
        id = id,
        username = username,
        email = email,
        phone = phone,
        location = location,
    )

    UserType.Doctor -> User.Doctor(
        id = id,
        username = username,
        email = email,
        phone = phone,
        location = location,
    )
}

fun User.toUserEntity() = UserEntity(
    id = id,
    username = username,
    email = email,
    phone = phone,
    location = location,
    type = type,
)
