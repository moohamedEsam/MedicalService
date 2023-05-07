package com.example.network

import com.example.common.models.Result
import com.example.model.app.Credentials
import com.example.model.app.Disease
import com.example.model.app.DiseaseView
import com.example.model.app.DonationRequest
import com.example.model.app.Medicine
import com.example.model.app.Register
import com.example.model.app.Symptom
import com.example.model.app.Token
import com.example.model.app.Transaction
import com.example.model.app.User.Donor
import com.example.model.app.User.Receiver

sealed interface RemoteDataSource {
    suspend fun login(credentials: Credentials): Result<Token>
    suspend fun register(register: Register): Result<Unit>

    suspend fun getDonationRequests(): Result<List<DonationRequest>>
    suspend fun getMedicines(): Result<List<Medicine>>

    suspend fun getDiseases(): Result<List<Disease>>

    suspend fun getSymptoms(): Result<List<Symptom>>

    suspend fun predictDiseaseBySymptoms(symptoms: List<Symptom>): Result<DiseaseView>

    suspend fun getUserTransactions(): Result<List<Transaction>>

    suspend fun getDonnerUser(id: String): Result<Donor>

    suspend fun getReceiverUser(id: String): Result<Receiver>
}