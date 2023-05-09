package com.example.domain

import com.example.common.functions.saveTokenToSharedPref
import com.example.data.auth.AuthRepository
import com.example.data.disease.DiseaseRepository
import com.example.data.donation.DonationRepository
import com.example.data.medicine.MedicineRepository
import com.example.data.transaction.TransactionRepository
import com.example.domain.usecase.disease.GetAvailableSymptomsUseCase
import com.example.domain.usecase.disease.GetDiseaseDetailsUseCase
import com.example.domain.usecase.disease.PredictDiseaseBySymptomsUseCase
import com.example.domain.usecase.donationRequest.GetDonationRequestByIdUseCase
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.domain.usecase.medicine.GetMedicineDetailsUseCase
import com.example.domain.usecase.medicine.GetMedicinesUseCase
import com.example.domain.usecase.sync.OneTimeSyncWorkUseCase
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.domain.usecase.user.LoginUseCase
import com.example.domain.usecase.user.RegisterUseCase
import com.example.model.app.DiseaseView
import com.example.model.app.Symptom
import com.example.model.app.User
import com.example.model.app.dummyList
import com.example.model.app.emptyDonor
import com.example.model.app.headache
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope

@Module
@ComponentScan
class DomainModule {
    @Factory
    fun provideRegisterUseCase(repository: AuthRepository) = RegisterUseCase(repository::register)

    context(Scope)
            @Factory
    fun provideLoginUseCase(
        repository: AuthRepository,
        oneTimeSyncWorkUseCase: OneTimeSyncWorkUseCase
    ) = LoginUseCase {
        val result = repository.login(it)
        result.ifSuccess { token ->
            saveTokenToSharedPref(androidContext(), token.token)
            oneTimeSyncWorkUseCase()
        }
        result

    }

    @Factory
    fun provideCurrentUserUseCase() = GetCurrentUserUseCase {
        User.emptyDonor().copy(username = "mohamed")
    }

    @Factory
    fun provideDiseaseDetailsUseCase(diseaseRepository: DiseaseRepository) =
        GetDiseaseDetailsUseCase(diseaseRepository::getDiseaseDetails)

    @Factory
    fun provideMedicineDetailsUseCase(medicineRepository: MedicineRepository) =
        GetMedicineDetailsUseCase(medicineRepository::getMedicineDetails)

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
    fun provideSetDonationRequestBookmarkUseCase(donationRepository: DonationRepository) =
        SetDonationRequestBookmarkUseCase(donationRepository::setDonationRequestBookmark)


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

    @Factory
    fun provideCreateTransactionUseCase(transactionRepository: TransactionRepository) =
        CreateTransactionUseCase(transactionRepository::insertTransaction)
}