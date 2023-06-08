package com.example.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.WorkerParameters
import com.example.data.diagnosis.request.DiagnosisRequestRepository
import com.example.data.diagnosis.result.DiagnosisResultRepository
import com.example.data.disease.DiseaseRepository
import com.example.data.donation.DonationRepository
import com.example.data.medicine.MedicineRepository
import com.example.data.transaction.TransactionRepository
import com.example.data.user.UserRepository

class SyncWorker(
    private val context: Context,
    params: WorkerParameters,
    private val medicineRepository: MedicineRepository,
    private val donationRepository: DonationRepository,
    private val diseaseRepository: DiseaseRepository,
    private val transactionRepository: TransactionRepository,
    private val diagnosisRequestRepository: DiagnosisRequestRepository,
    private val diagnosisResultRepository: DiagnosisResultRepository,
    private val userRepository: UserRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = try {
        val isSyncSuccessful = listOf(
            diseaseRepository.syncDiseases(),
            medicineRepository.syncMedicines(),
            userRepository.syncUser(),
            transactionRepository.syncTransactions(),
            donationRepository.syncDonationRequests(),
            diagnosisRequestRepository.syncDiagnosisRequest(),
            diagnosisResultRepository.syncDiagnosis()
        ).all { it }

        if (isSyncSuccessful)
            Result.success()
        else
            Result.failure()
    } catch (e: Exception) {
        Log.i("SyncWorker", "doWork: ${e.message}")
        Result.failure()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo = context.syncForegroundInfo()

    companion object {
        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        const val workName = "SyncWorker"
    }
}