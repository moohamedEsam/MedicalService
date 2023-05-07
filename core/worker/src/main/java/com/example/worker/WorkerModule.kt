package com.example.worker

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker{
        SyncWorker(
            context = androidApplication(),
            params = get(),
            diseaseRepository = get(),
            medicineRepository = get(),
            donationRepository = get(),
            transactionRepository = get()
        )
    }
}