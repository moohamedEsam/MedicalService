package com.example.medicalservice.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteResponse<T>(
    val data: T? = null,
    @SerialName("arr")
    val error: String? = null,
    @SerialName("success")
    val isSuccess: Boolean = false,
)