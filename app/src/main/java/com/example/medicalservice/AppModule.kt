package com.example.medicalservice

import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.functions.snackbar.BaseSnackBarManager
import com.example.functions.snackbar.SnackBarManager
import com.example.medicalservice.domain.*
import com.example.models.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

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
            recentTransactions = listOf(
                Transaction.empty(),
                Transaction.empty(),
                Transaction.empty(),
                Transaction.empty(),
            )
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
        listOf(
            DonationRequest.empty().copy(
                needed = 10,
                collected = 4,
                contributorsCount = 3,
                medicine = Medicine.empty().copy(
                    name = "Paracetamol",
                    description = "Paracetamol is a painkiller and a fever reducer (antipyretic). It is used to treat many conditions such as headache, muscle aches, arthritis, backache, toothaches, colds, and fevers. It is also used to treat pain and fever after surgery. Paracetamol is in a class of medications called analgesics (pain relievers) and antipyretics (fever reducers). It works by blocking the release of certain chemical messengers that cause pain and fever in the body."
                ),
            )
        )
    }

    @Single([SnackBarManager::class])
    fun provideSnackBarManager() = BaseSnackBarManager()

    @Factory
    fun provideGetAvailableSymptomsUseCase() = GetAvailableSymptomsUseCase {
        Symptom.dummyList()
    }
}

