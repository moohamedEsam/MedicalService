package com.example.domain

import com.example.data.AuthRepository
import com.example.domain.usecase.GetAvailableSymptomsUseCase
import com.example.domain.usecase.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.GetDiseaseDetailsUseCase
import com.example.domain.usecase.GetDonationRequestsUseCase
import com.example.domain.usecase.GetMedicineDetailsUseCase
import com.example.domain.usecase.GetMedicinesUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.PredictDiseaseBySymptomsUseCase
import com.example.domain.usecase.RegisterUseCase
import com.example.model.app.DiseaseView
import com.example.model.app.Medicine
import com.example.model.app.MedicineView
import com.example.model.app.Symptom
import com.example.model.app.Transaction
import com.example.model.app.User
import com.example.model.app.dummyDonationRequests
import com.example.model.app.dummyList
import com.example.model.app.empty
import com.example.model.app.emptyDonor
import com.example.model.app.headache
import com.example.model.app.paracetamol
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import kotlin.random.Random

@Module
@ComponentScan
class DomainModule{
    @Factory
    fun provideRegisterUseCase(repository: AuthRepository) = RegisterUseCase(repository::register)

    @Factory
    fun provideLoginUseCase(repository: AuthRepository) = LoginUseCase(repository::login)

    @Factory
    fun provideCurrentUserUseCase() = GetCurrentUserUseCase {
        User.emptyDonor().copy(username = "mohamed")
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
        dummyDonationRequests()
    }


    @Factory
    fun provideGetAvailableSymptomsUseCase() = GetAvailableSymptomsUseCase {
        Symptom.dummyList()
    }

    @Factory
    fun providePredictDiseasesUseCase() = PredictDiseaseBySymptomsUseCase {
        listOf(DiseaseView.headache())
    }


    @Factory
    fun provideGetCurrentUserTransactionsUseCase() = GetCurrentUserTransactionsUseCase {
        List(Random.nextInt(1,100)) {
            Transaction.empty().copy(
                medicine = Medicine.empty().copy(
                    name = "Paracetamol",
                    description = "Paracetamol is a painkiller and a fever reducer (antipyretic). It is used to treat many conditions such as headache, muscle aches, arthritis, backache, toothaches, colds, and fevers. It is also used to treat pain and fever after surgery. Paracetamol is in a class of medications called analgesics (pain relievers) and antipyretics (fever reducers). It works by blocking the release of certain chemical messengers that cause pain and fever in the body."
                ),
                quantity = Random.nextInt(1, 1000),
                senderName = "mohamed",
                receiverName = "medical service",
            )
        }
    }
}