package com.example.network

import com.example.common.models.Result
import com.example.model.app.auth.Credentials
import com.example.model.app.auth.Token
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.disease.Disease
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.Symptom
import com.example.model.app.donation.DonationRequest
import com.example.model.app.medicine.Medicine
import com.example.model.app.transaction.Transaction
import com.example.model.app.user.Register
import com.example.model.app.user.User
import com.example.model.app.user.User.Donor
import com.example.model.app.user.User.Receiver

sealed interface RemoteDataSource {
    suspend fun login(credentials: Credentials): Result<Token>
    suspend fun register(register: Register): Result<Unit>

    suspend fun getDonationRequests(): Result<List<DonationRequest>>
    suspend fun getMedicines(): Result<List<Medicine>>

    suspend fun getDiseases(): Result<List<Disease>>

    suspend fun getSymptoms(): Result<List<Symptom>>

    suspend fun predictDiseaseBySymptoms(symptoms: List<Symptom>): Result<DiseaseView>

    suspend fun getUserTransactions(): Result<List<Transaction>>

    suspend fun getTransactions(): Result<List<Transaction>>

    suspend fun createTransaction(transaction: Transaction): Result<Unit>

    suspend fun updateTransaction(transaction: Transaction): Result<Unit>

    suspend fun getDonnerUser(id: String): Result<Donor>

    suspend fun getReceiverUser(id: String): Result<Receiver>

    suspend fun getCurrentUser(email: String): Result<User>

    suspend fun getAllUsers(): Result<List<User>>

    suspend fun getCurrentUserDiagnosisRequests(): Result<List<DiagnosisRequest>>

    suspend fun getCurrentUserDiagnosisResults(): Result<List<DiagnosisResult>>

    suspend fun createDiagnosisRequest(donationRequest: DiagnosisRequest): Result<Unit>

    suspend fun updateDiagnosisRequest(donationRequest: DiagnosisRequest): Result<Unit>

    suspend fun createDiagnosisResult(donationRequest: DiagnosisResult): Result<Unit>

    suspend fun updateDiagnosisResult(donationRequest: DiagnosisResult): Result<Unit>
}