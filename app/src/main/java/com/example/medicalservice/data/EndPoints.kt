package com.example.medicalservice.data

object EndPoints {
    private const val BASE_URL = "http://192.168.1.2:3000/api"

    const val LOGIN = "$BASE_URL/users/auth/login"
    const val REGISTER = "$BASE_URL/users/adduser"
}