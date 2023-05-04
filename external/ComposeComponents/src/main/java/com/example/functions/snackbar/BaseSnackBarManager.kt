package com.example.functions.snackbar

import com.example.common.models.SnackBarEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class BaseSnackBarManager : SnackBarManager {
    private val snackBarEventChannel = Channel<SnackBarEvent>()

    override fun getReceiverChannel(): Flow<SnackBarEvent> = snackBarEventChannel.receiveAsFlow()
    override suspend fun showSnackBarEvent(event: SnackBarEvent) = snackBarEventChannel.send(event)
}