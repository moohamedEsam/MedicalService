package com.example.domain

import com.example.data.auth.AuthRepository
import com.example.data.donation.DonationRepository
import com.example.data.transaction.TransactionRepository
import com.example.domain.usecase.GetAvailableSymptomsUseCase
import com.example.domain.usecase.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.GetCurrentUserUseCase
import com.example.domain.usecase.GetDiseaseDetailsUseCase
import com.example.domain.usecase.GetDonationRequestByIdUseCase
import com.example.domain.usecase.GetDonationRequestsUseCase
import com.example.domain.usecase.GetMedicineDetailsUseCase
import com.example.domain.usecase.GetMedicinesUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.PredictDiseaseBySymptomsUseCase
import com.example.domain.usecase.RegisterUseCase
import com.example.model.app.DiseaseView
import com.example.model.app.MedicineView
import com.example.model.app.Symptom
import com.example.model.app.User
import com.example.model.app.dummyList
import com.example.model.app.emptyDonor
import com.example.model.app.headache
import com.example.model.app.paracetamol
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class DomainModule {
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
    fun provideDonationRequestsUseCase(donationRepository: DonationRepository) =
        GetDonationRequestsUseCase(donationRepository::getDonationRequests)

    @Factory
    fun provideDonationRequestByIdUseCase(donationRepository: DonationRepository) =
        GetDonationRequestByIdUseCase(donationRepository::getDonationRequest)


    @Factory
    fun provideGetAvailableSymptomsUseCase() = GetAvailableSymptomsUseCase {
        Symptom.dummyList()
    }

    @Factory
    fun providePredictDiseasesUseCase() = PredictDiseaseBySymptomsUseCase {
        listOf(DiseaseView.headache())
    }


    @Factory
    fun provideGetCurrentUserTransactionsUseCase(transactionRepository: TransactionRepository) =
        GetCurrentUserTransactionsUseCase(transactionRepository::getTransactions)
}