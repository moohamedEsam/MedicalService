package com.example.functions.snackbar

import com.example.common.models.SnackBarEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSnackBarManager : SnackBarManager {

    override fun getReceiverChannel(): Flow<SnackBarEvent> = flowOf()
    override suspend fun showSnackBarEvent(event: SnackBarEvent) = Unit
}