package com.example.domain

import android.content.Context
import android.util.Log
import androidx.paging.PagingData
import com.example.common.functions.loadToken
import com.example.common.functions.saveToken
import com.example.common.models.Result
import com.example.data.auth.AuthRepository
import com.example.data.disease.DiseaseRepository
import com.example.data.donation.DonationRepository
import com.example.data.medicine.MedicineRepository
import com.example.data.transaction.TransactionRepository
import com.example.data.user.UserRepository
import com.example.datastore.dataStore
import com.example.domain.usecase.diagnosis.CreateDiagnosisRequestUseCase
import com.example.domain.usecase.diagnosis.GetDiagnosisResultByIdUseCase
import com.example.domain.usecase.diagnosis.GetUserLatestDiagnosisUseCase
import com.example.domain.usecase.disease.GetAvailableSymptomsUseCase
import com.example.domain.usecase.disease.GetDiseaseDetailsUseCase
import com.example.domain.usecase.disease.PredictDiseaseBySymptomsUseCase
import com.example.domain.usecase.donationRequest.GetBookmarkedDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.GetDonationRequestByIdUseCase
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.domain.usecase.medicine.GetMedicineDetailsUseCase
import com.example.domain.usecase.medicine.GetMedicinesUseCase
import com.example.domain.usecase.sync.OneTimeSyncWorkUseCase
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import com.example.domain.usecase.transaction.GetTransactionsUseCase
import com.example.domain.usecase.user.GetCurrentUserIdUseCase
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.domain.usecase.user.IsUserLoggedInUseCase
import com.example.domain.usecase.user.LoginUseCase
import com.example.domain.usecase.user.RegisterUseCase
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.diagnosis.empty
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.Symptom
import com.example.model.app.disease.dummyList
import com.example.model.app.disease.headache
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.empty
import com.example.model.app.medicine.paracetamol
import com.example.model.app.user.User
import com.example.model.app.user.emptyDoctor
import com.example.model.app.user.emptyDonor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope
import java.util.Date

@Module
@ComponentScan
class DomainModule {

    @Factory
    fun provideCurrentUserIdUseCase(userRepository: UserRepository, context: Context) =
        GetCurrentUserIdUseCase {
            val email = context.dataStore.data.firstOrNull()?.email ?: ""
            userRepository.getCurrentUserId(email) ?: ""
        }

    @Factory
    fun provideLoginUseCase(
        repository: AuthRepository,
        oneTimeSyncWorkUseCase: OneTimeSyncWorkUseCase,
        context: Context
    ) = LoginUseCase {
        val result = repository.login(it)
        result.ifSuccess { token ->
            context.saveToken(token.token)
            context.dataStore.updateData { userSettings ->
                userSettings.copy(token = token.token, email = it.email)
            }
            oneTimeSyncWorkUseCase()
        }
        result

    }

    @Factory
    fun provideRegisterUseCase(repository: AuthRepository) = RegisterUseCase(repository::register)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Factory
    fun provideCurrentUserUseCase(userRepository: UserRepository, context: Context) =
        GetCurrentUserUseCase {
            context.dataStore.data.filterNotNull().flatMapLatest {
                userRepository.getCurrentUser(it.email)
            }
        }

    context (Scope)
            @Factory
    fun provideIsUseLoggedInUseCase() = IsUserLoggedInUseCase {
        androidContext().loadToken()?.isNotEmpty() == true
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
    fun provideGetBookmarkedDonationRequestsUseCase(donationRepository: DonationRepository) =
        GetBookmarkedDonationRequestsUseCase(donationRepository::getBookmarkedDonationRequests)


    @Factory
    fun provideGetAvailableSymptomsUseCase() = GetAvailableSymptomsUseCase {
        Symptom.dummyList()
    }

    @Factory
    fun providePredictDiseasesUseCase() = PredictDiseaseBySymptomsUseCase {
        listOf(DiseaseView.headache())
    }


    @Factory
    fun provideGetCurrentUserTransactionsUseCase(
        transactionRepository: TransactionRepository,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    ) = GetCurrentUserTransactionsUseCase {
        Log.i("DomainModule", "provideGetCurrentUserTransactionsUseCase: called")
        val id = getCurrentUserIdUseCase()
        transactionRepository.getTransactionsByUserId(id)
    }

    @Factory
    fun provideGetTransactionsUseCase(transactionRepository: TransactionRepository) =
        GetTransactionsUseCase(transactionRepository::getTransactions)

    @Factory
    fun provideDeleteTransactionUseCase(transactionRepository: TransactionRepository) =
        DeleteTransactionUseCase(transactionRepository::deleteTransaction)

    @Factory
    fun provideGetTransactionDetailsUseCase(transactionRepository: TransactionRepository) =
        GetTransactionDetailsUseCase(transactionRepository::getTransaction)

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

    @Factory
    fun provideCreateDiagnosisRequestUseCase() = CreateDiagnosisRequestUseCase {
        Result.Success(it)
    }

    @Factory
    fun provideGetDiagnosisResultByIdUseCase() = GetDiagnosisResultByIdUseCase {
        val diagnosis =
            "Acute sinusitis is an inflammation of the sinuses, which are air-filled cavities located in the bones of the skull. The sinuses are lined with a thin layer of mucus membrane, which helps to trap dust, pollen, and other particles from the air. When this mucus membrane becomes inflamed, it can produce more mucus, which can block the sinus openings and cause pain, pressure, and swelling."
        val doctor: User.Doctor = User.emptyDoctor().copy(username = "Dr. Smith")
        val status: DiagnosisResult.Status = DiagnosisResult.Status.Pending
        val id = "1234567890"
        val createdAt = Date()
        val updatedAt = Date()
        val diagnosisResultView = DiagnosisResultView(
            diagnosis = diagnosis,
            doctor = doctor,
            status = status,
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt,
            diagnosisRequest = DiagnosisRequest.empty().copy(
                symptoms = listOf(
                    Symptom("Headache"),
                    Symptom("Fever"),
                    Symptom("Nasal congestion"),
                    Symptom("Facial pain")
                ),
            ),
            medications = listOf(
                MedicineView.empty().copy(name = "Nasal decongestant"),
                MedicineView.empty().copy(name = "Pain reliever"),
                MedicineView.empty().copy(name = "Antihistamine"),
            )
        )
        flowOf(diagnosisResultView)
    }
}