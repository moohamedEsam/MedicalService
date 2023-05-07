package com.example.domain.usecase

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo

fun interface OneTimeSyncWorkUseCase : () -> LiveData<WorkInfo>