package com.example.medicalservice.data

object EndPoints {
    private const val BASE_URL = "http://localhost:3000/api"

    const val LOGIN = "$BASE_URL/users/auth/login"
    const val REGISTER = "$BASE_URL/register"
}