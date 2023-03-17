package com.example.medicalservice

import android.app.Application
import com.example.auth.AuthModule
import com.example.maplocation.MapLocationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.*

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                listOf(
                    AuthModule().module,
                    MapLocationModule().module,
                    AppModule().module
                )
            )

            androidLogger()
            androidContext(this@ApplicationClass)
        }
    }
}