package com.example.domain.usecase.settings

import kotlinx.coroutines.flow.Flow

fun interface ObserveIpUseCase : () -> Flow<String>