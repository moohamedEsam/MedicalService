package com.example.functions.snackbar

import com.example.common.models.SnackBarEvent
import kotlinx.coroutines.flow.Flow

interface SnackBarManager {
    fun getReceiverChannel(): Flow<SnackBarEvent>

    suspend fun showSnackBarEvent(event: SnackBarEvent)
}