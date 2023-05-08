package com.example.domain.usecase.sync

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo

fun interface OneTimeSyncWorkUseCase : () -> LiveData<WorkInfo>