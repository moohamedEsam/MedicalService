package com.example.model.app.user


data class CreateUserDto(
    val username: String,
    val email: String,
    val password: String,
    val phone:String,
    val type: UserType,
    val location: Location,
)
