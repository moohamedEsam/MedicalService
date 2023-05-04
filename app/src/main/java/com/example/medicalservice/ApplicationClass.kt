package com.example.medicalservice

import android.app.Application
import com.example.auth.AuthModule
import com.example.data.DataModule
import com.example.database.DatabaseModule
import com.example.domain.DomainModule
import com.example.maplocation.MapLocationModule
import com.example.network.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                listOf(
                    DomainModule().module,
                    MapLocationModule().module,
                    AppModule().module,
                    DomainModule().module,
                    NetworkModule().module,
                    DataModule().module,
                    AuthModule().module,
                    DatabaseModule().module
                )
            )

            androidLogger()
            androidContext(this@ApplicationClass)
        }
    }
}