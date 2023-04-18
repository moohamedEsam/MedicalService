package com.example.medicalservice.data.models

data class RemoteResponse<T>(
    val data: T? = null,
    val error: String? = null,
    val isSuccess: Boolean = false,
)