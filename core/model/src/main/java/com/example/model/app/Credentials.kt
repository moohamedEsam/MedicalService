package com.example.model.app

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(val email: String, val password: String)
