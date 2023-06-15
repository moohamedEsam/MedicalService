package com.example.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.example.common.functions.loadToken
import com.example.common.functions.saveToken
import com.example.common.models.Result
import com.example.data.auth.AuthRepository
import com.example.data.diagnosis.request.DiagnosisRequestRepository
import com.example.data.diagnosis.result.DiagnosisResultRepository
import com.example.data.disease.DiseaseRepository
import com.example.data.donation.DonationRepository
import com.example.data.medicine.MedicineRepository
import com.example.data.transaction.TransactionRepository
import com.example.data.user.UserRepository
import com.example.datastore.dataStore
import com.example.domain.usecase.diagnosis.CreateDiagnosisRequestUseCase
import com.example.domain.usecase.diagnosis.CreateDiagnosisResultUseCase
import com.example.domain.usecase.diagnosis.ExtractPrescriptionFromImageUseCase
import com.example.domain.usecase.diagnosis.GetDiagnosisResultByIdUseCase
import com.example.domain.usecase.diagnosis.GetDiagnosisResultsUseCase
import com.example.domain.usecase.diagnosis.GetUserLatestDiagnosisUseCase
import com.example.domain.usecase.diagnosis.UpdateDiagnosisResultUseCase
import com.example.domain.usecase.disease.CreateDiseaseUseCase
import com.example.domain.usecase.disease.GetAvailableSymptomsUseCase
import com.example.domain.usecase.disease.GetDiseaseDetailsUseCase
import com.example.domain.usecase.disease.GetDiseasesUseCase
import com.example.domain.usecase.donationRequest.GetBookmarkedDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.GetDonationRequestByIdUseCase
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.domain.usecase.medicine.CreateMedicineUseCase
import com.example.domain.usecase.medicine.GetMedicineDetailsUseCase
import com.example.domain.usecase.settings.ChangeIpUseCase
import com.example.domain.usecase.settings.ObserveIpUseCase
import com.example.domain.usecase.sync.OneTimeSyncWorkUseCase
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.transaction.GetRecentTransactionsUseCase
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import com.example.domain.usecase.transaction.GetTransactionsUseCase
import com.example.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.domain.usecase.user.CallPhoneNumberUseCase
import com.example.domain.usecase.user.GetCurrentUserIdUseCase
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.domain.usecase.user.IsUserLoggedInUseCase
import com.example.domain.usecase.user.LogOutUseCase
import com.example.domain.usecase.user.LoginUseCase
import com.example.domain.usecase.user.RegisterUseCase
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.empty
import com.example.model.app.disease.Symptom
import com.example.model.app.disease.dummyList
import com.example.model.app.transaction.TransactionView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope

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
    
    context (Scope)
    @Factory
    fun provideLogoutUseCase(authRepository: AuthRepository) = LogOutUseCase {
        authRepository.logout()
        androidContext().saveToken("")
        androidContext().dataStore.updateData { userSettings ->
            userSettings.copy(token = "", email = "")
        }
        Result.Success(Unit)
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
    fun provideGetCurrentUserTransactionsUseCase(
        transactionRepository: TransactionRepository,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    ) = GetCurrentUserTransactionsUseCase {
        val id = getCurrentUserIdUseCase()
        transactionRepository.getTransactionsByUserId(id)
    }

    @Factory
    fun provideGetTransactionsUseCase(transactionRepository: TransactionRepository) =
        GetTransactionsUseCase(transactionRepository::getTransactions)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Factory
    fun provideGetRecentTransactionsUseCase(
        transactionRepository: TransactionRepository,
        getCurrentUserUseCase: GetCurrentUserUseCase
    ) = GetRecentTransactionsUseCase {
        getCurrentUserUseCase().map { it.id }.flatMapLatest {
            transactionRepository.getRecentTransactions(it)
        }
    }


    @Factory
    fun provideDeleteTransactionUseCase(transactionRepository: TransactionRepository) =
        DeleteTransactionUseCase(transactionRepository::deleteTransaction)

    @Factory
    fun provideUpdateTransactionUseCase(transactionRepository: TransactionRepository) =
        UpdateTransactionUseCase {
            if (it.isDelivered && it.isReceived)
                transactionRepository.updateTransaction(it.copy(status = TransactionView.Status.Completed))
            else
                transactionRepository.updateTransaction(it)
        }

    @Factory
    fun provideGetTransactionDetailsUseCase(transactionRepository: TransactionRepository) =
        GetTransactionDetailsUseCase(transactionRepository::getTransaction)

    @Factory
    fun provideCreateTransactionUseCase(transactionRepository: TransactionRepository) =
        CreateTransactionUseCase(transactionRepository::insertTransaction)


    @Factory
    fun provideGetLatestDiagnosisRequestUseCase(diagnosisResultRepository: DiagnosisResultRepository) =
        GetUserLatestDiagnosisUseCase(diagnosisResultRepository::getLatestDiagnosisResult)

    @Factory
    fun provideCreateDiagnosisRequestUseCase(
        diagnosisRequestRepository: DiagnosisRequestRepository,
        diagnosisResultRepository: DiagnosisResultRepository
    ) = CreateDiagnosisRequestUseCase {
        val result = diagnosisRequestRepository.insertDiagnosisRequest(it)
        result.ifSuccess { diagnosisRequest ->
            val diagnosisResult = DiagnosisResult.empty().copy(
                status = DiagnosisResult.Status.Pending,
                diagnosisRequestId = diagnosisRequest.id
            )
            diagnosisResultRepository.insertDiagnosis(diagnosisResult)
        }
        result
    }

    @Factory
    fun provideGetDiagnosisResultByIdUseCase(diagnosisResultRepository: DiagnosisResultRepository) =
        GetDiagnosisResultByIdUseCase(diagnosisResultRepository::getDiagnosis)

    @Factory
    fun provideExtractImageTextUseCase(context: Context) =
        ExtractPrescriptionFromImageUseCase { uri ->
            val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromFilePath(context, uri.toUri())
            textRecognizer.process(image)
        }

    @Factory
    fun provideGetDiagnosisResultsView(diagnosisResultRepository: DiagnosisResultRepository) =
        GetDiagnosisResultsUseCase(diagnosisResultRepository::getDiagnosisResultsView)

    @Factory
    fun provideGetDiseasesUseCase(diseaseRepository: DiseaseRepository) =
        GetDiseasesUseCase(diseaseRepository::getDiseasesFlow)

    @Factory
    fun provideCreateDiseaseUseCase(diseaseRepository: DiseaseRepository) =
        CreateDiseaseUseCase(diseaseRepository::insertDisease)

    @Factory
    fun provideCreateMedicineUseCase(medicineRepository: MedicineRepository) =
        CreateMedicineUseCase(medicineRepository::createMedicine)

    @Factory
    fun provideUpdateDiagnosisResultUseCase(diagnosisResultRepository: DiagnosisResultRepository) =
        UpdateDiagnosisResultUseCase(diagnosisResultRepository::updateDiagnosis)

    context(Scope)
            @Factory
    fun provideChangeIpUseCase() = ChangeIpUseCase {
        androidContext().dataStore.updateData { userSettings ->
            userSettings.copy(remoteServerIp = it)
        }
    }

    context(Scope)
            @Factory
    fun provideGetIpUseCase() = ObserveIpUseCase {
        androidContext().dataStore.data.map { it.remoteServerIp }
    }

    @Factory
    fun provideCreateDiagnosisResultUseCase(diagnosisResultRepository: DiagnosisResultRepository) =
        CreateDiagnosisResultUseCase(diagnosisResultRepository::insertDiagnosis)

    context (Scope)
            @Factory
    fun provideCallPhoneUseCase() = CallPhoneNumberUseCase {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        androidContext().startActivity(intent)
    }
}