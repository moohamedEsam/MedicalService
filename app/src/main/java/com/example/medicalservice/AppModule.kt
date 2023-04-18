package com.example.medicalservice

import android.os.Build
import android.util.Log
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.functions.snackbar.BaseSnackBarManager
import com.example.functions.snackbar.SnackBarManager
import com.example.medicalservice.domain.*
import com.example.models.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import kotlin.random.Random

@Module
@ComponentScan
class AppModule {

    context(Scope)
            @Single
    fun provideImageLoader() = ImageLoader.Builder(androidContext())
        .crossfade(true)
        .components {
            if (Build.VERSION.SDK_INT >= 28)
                add(ImageDecoderDecoder.Factory())
            else
                add(GifDecoder.Factory())
        }
        .build()

    @Factory
    fun provideCurrentUserUseCase() = GetCurrentUserUseCase {
        User.emptyDonor().copy(
            recentTransactions = List(4) {
                Transaction.empty().copy(
                    medicine = Medicine.empty().copy(
                        name = "Paracetamol",
                        description = "Paracetamol is a painkiller and a fever reducer (antipyretic). It is used to treat many conditions such as headache, muscle aches, arthritis, backache, toothaches, colds, and fevers. It is also used to treat pain and fever after surgery. Paracetamol is in a class of medications called analgesics (pain relievers) and antipyretics (fever reducers). It works by blocking the release of certain chemical messengers that cause pain and fever in the body."
                    ),
                    quantity = Random.nextInt(1, 1000),
                    senderName = "mohamed",
                    receiverName = "medical service",
                )
            },
            username = "mohamed",
        )
    }

    @Factory
    fun provideDiseaseDetailsUseCase() = GetDiseaseDetailsUseCase {
        DiseaseView.headache()
    }

    @Factory
    fun provideMedicineDetailsUseCase() = GetMedicineDetailsUseCase {
        MedicineView.paracetamol()
    }

    @Factory
    fun provideMedicinesUseCase() = GetMedicinesUseCase {
        listOf(MedicineView.paracetamol())
    }

    @Factory
    fun provideDonationRequestsUseCase() = GetDonationRequestsUseCase {
        List(10) {
            val needed = Random.nextInt(1, 1000)
            val collected = Random.nextInt(1, needed)
            DonationRequest.empty().copy(
                needed = needed,
                collected = collected,
                contributorsCount = Random.nextInt(1, 100),
                medicine = Medicine.empty().copy(
                    name = "Paracetamol",
                    description = "Paracetamol is a painkiller and a fever reducer (antipyretic). It is used to treat many conditions such as headache, muscle aches, arthritis, backache, toothaches, colds, and fevers. It is also used to treat pain and fever after surgery. Paracetamol is in a class of medications called analgesics (pain relievers) and antipyretics (fever reducers). It works by blocking the release of certain chemical messengers that cause pain and fever in the body."
                ),
            )
        }

    }

    @Single([SnackBarManager::class])
    fun provideSnackBarManager() = BaseSnackBarManager()

    @Factory
    fun provideGetAvailableSymptomsUseCase() = GetAvailableSymptomsUseCase {
        Symptom.dummyList()
    }

    @Factory
    fun providePredictDiseasesUseCase() = PredictDiseaseBySymptomsUseCase {
        listOf(DiseaseView.headache())
    }

    @Factory
    fun provideCoroutineExceptionHandler() = CoroutineExceptionHandler { _, throwable ->
        Log.e("Error", "CoroutineExceptionHandler: ${throwable.message}")
        throwable.printStackTrace()
    }

    @Single
    fun provideHttpClient() = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i("ktor", "log: $message")
                }
            }
        }
    }

}

