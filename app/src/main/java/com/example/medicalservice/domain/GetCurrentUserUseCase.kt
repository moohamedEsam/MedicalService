package com.example.medicalservice.domain

import com.example.models.User

fun interface GetCurrentUserUseCase : suspend () -> User