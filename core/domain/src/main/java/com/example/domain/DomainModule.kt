package com.example.domain

import com.example.common.functions.saveToken
import com.example.common.models.Result
import com.example.data.auth.AuthRepository
import com.example.data.disease.DiseaseRepository
import com.example.data.donation.DonationRepository
import com.example.data.medicine.MedicineRepository
import com.example.data.transaction.TransactionRepository
import com.example.datastore.dataStore
import com.example.domain.usecase.diagnosis.CreateDiagnosisRequestUseCase
import com.example.domain.usecase.diagnosis.GetDiagnosisResultByIdUseCase
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
import com.example.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import com.example.domain.usecase.transaction.GetTransactionsUseCase
import com.example.domain.usecase.user.GetCurrentUserUseCase
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
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
    fun provideRegisterUseCase(repository: AuthRepository) = RegisterUseCase(repository::register)

    context (Scope)
            @Factory
    fun provideGetCurrentUserUseCase(repository: AuthRepository) = GetCurrentUserUseCase {
        val email = androidContext().dataStore.data.map { it.email }.lastOrNull()
            ?: return@GetCurrentUserUseCase Result.Error("No user found")
        val result = repository.getCurrentUser(email)
        result.ifSuccess { user ->
            androidContext().dataStore.updateData { userSettings ->
                userSettings.copy(type = user.type)
            }
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
            androidContext().saveToken(token.token)
            androidContext().dataStore.updateData { userSettings ->
                userSettings.copy(token = token.token, email = it.email, password = it.password)
            }
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
        GetCurrentUserTransactionsUseCase(transactionRepository::getTransactionsByUserId)

    @Factory
    fun provideGetTransactionsUseCase(transactionRepository: TransactionRepository) = GetTransactionsUseCase(transactionRepository::getTransactions)

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