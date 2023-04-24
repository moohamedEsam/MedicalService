package com.example.medicalservice.domain

import com.example.models.app.User

fun interface GetCurrentUserUseCase : suspend () -> User