package com.example.medicalservice

import android.app.Application
import com.example.auth.di.authModule
import com.example.maplocation.mapLocationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                listOf(
                    authModule,
                    appModule,
                    mapLocationModule
                )
            )
            androidContext(this@ApplicationClass)
        }
    }
}