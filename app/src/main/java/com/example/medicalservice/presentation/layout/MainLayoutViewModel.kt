package com.example.medicalservice.presentation.layout

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.common.models.SnackBarEvent
import com.example.domain.usecase.OneTimeSyncWorkUseCase
import com.example.functions.snackbar.SnackBarManager
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainLayoutViewModel(
    private val oneTimeSyncWorkUseCase: OneTimeSyncWorkUseCase,
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    fun sync(owner: LifecycleOwner) {
        oneTimeSyncWorkUseCase.invoke().observe(owner) {
            viewModelScope.launch {
                when (it.state) {
                    WorkInfo.State.ENQUEUED -> snackBarManager.showSnackBarEvent(SnackBarEvent("Sync Enqueued"))
                    WorkInfo.State.RUNNING -> snackBarManager.showSnackBarEvent(SnackBarEvent("Syncing..."))
                    WorkInfo.State.SUCCEEDED -> snackBarManager.showSnackBarEvent(SnackBarEvent("Sync Success"))
                    WorkInfo.State.FAILED -> snackBarManager.showSnackBarEvent(SnackBarEvent("Sync Failed"))
                    else -> Unit
                }
            }
        }
    }
}