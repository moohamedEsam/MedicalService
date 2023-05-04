package com.example.network

import com.example.common.models.Result

sealed interface RemoteDataSource {
    suspend fun login(credentials: com.example.model.app.Credentials): Result<com.example.model.app.Token>
    suspend fun register(register: com.example.model.app.Register): Result<Unit>

    suspend fun getDonationRequests(): Result<List<com.example.model.app.DonationRequest>>
    suspend fun getMedicines(): Result<List<com.example.model.app.Medicine>>

    suspend fun getDiseases(): Result<List<com.example.model.app.Disease>>

    suspend fun getSymptoms(): Result<List<com.example.model.app.Symptom>>

    suspend fun getUserTransactions(id: String): Result<List<com.example.model.app.Transaction>>

    suspend fun getDonnerUser(id: String): Result<com.example.model.app.User.Donor>

    suspend fun getReceiverUser(id: String): Result<com.example.model.app.User.Receiver>
}