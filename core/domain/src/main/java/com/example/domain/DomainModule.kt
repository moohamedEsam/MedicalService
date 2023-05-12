package com.example.domain

import com.example.common.functions.SharedPrefsKey
import com.example.common.functions.loadFromSharedPrefs
import com.example.common.functions.saveToSharedPrefs
import com.example.common.functions.saveToken
import com.example.common.models.Result
import com.example.data.auth.AuthRepository
import com.example.data.disease.DiseaseRepository
import com.example.data.donation.DonationRepository
import com.example.data.medicine.MedicineRepository
import com.example.data.transaction.TransactionRepository
import com.example.domain.usecase.diagnosis.GetUserLatestDiagnosisUseCase
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
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.diagnosis.empty
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.Symptom
import com.example.model.app.disease.dummyList
import com.example.model.app.disease.headache
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import com.example.model.app.user.User
import com.example.model.app.user.emptyDoctor
import com.example.model.app.user.emptyDonor
import kotlinx.coroutines.flow.flowOf
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

    context (Scope)
            @Factory
    fun provideGetCurrentUserUseCase(repository: AuthRepository) = GetCurrentUserUseCase {
        val email = loadFromSharedPrefs(androidContext(), SharedPrefsKey.EMAIL)
        val result = repository.getCurrentUser(email ?: "")
        result.ifSuccess {
            saveToSharedPrefs(androidContext(), SharedPrefsKey.USER_TYPE, it.type.name)
        }
    }

    context(Scope)
            @Factory
    fun provideLoginUseCase(
        repository: AuthRepository,
        oneTimeSyncWorkUseCase: OneTimeSyncWorkUseCase
    ) = LoginUseCase {
        val result = repository.login(it)
        result.ifSuccess { token ->
            saveToken(androidContext(), token.token)
            saveToSharedPrefs(androidContext(), SharedPrefsKey.EMAIL, it.email)
            saveToSharedPrefs(androidContext(), SharedPrefsKey.PASSWORD, it.password)

            oneTimeSyncWorkUseCase()
        }
        result

    }

    @Factory
    fun provideCurrentUserUseCase() = GetCurrentUserUseCase {
        Result.Success(User.emptyDonor().copy(username = "mohamed"))
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


    @Factory
    fun provideGetLatestDiagnosisRequestUseCase() = GetUserLatestDiagnosisUseCase {
        flowOf(
            DiagnosisResultView.empty().copy(
                diagnosis = "Based on your symptoms, it sounds like you have a viral infection. This is a common cause of fever. The good news is that most viral infections go away on their own within a week or two.",
                doctor = User.emptyDoctor().copy(username = "Dr. John Doe")
            )
        )
    }
}