package com.example.network

import com.example.common.functions.tryWrapper
import com.example.common.models.Result
import com.example.network.models.RemoteResponse
import com.example.network.models.toNetworkRegister
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single

@Single(binds = [RemoteDataSource::class])
class KtorRemoteDataSource(
    private val client: HttpClient
) : RemoteDataSource {
    override suspend fun login(credentials: com.example.model.app.Credentials): Result<com.example.model.app.Token> = tryWrapper {
        val response = client.post(EndPoints.LOGIN) {
            setBody(credentials)
            contentType(ContentType.Application.Json)
        }
        mapResponse(response.body())
    }

    override suspend fun register(register: com.example.model.app.Register): Result<Unit> = tryWrapper {
        val response = client.post(EndPoints.REGISTER) {
            setBody(register.toNetworkRegister())
            contentType(ContentType.Application.Json)
        }
        mapResponse(response.body())
    }

    override suspend fun getDonationRequests(): Result<List<com.example.model.app.DonationRequestView>> = tryWrapper {
        val response = client.get(EndPoints.DONATION_REQUEST)
        mapResponse(response.body())
    }

    override suspend fun getMedicines(): Result<List<com.example.model.app.Medicine>> = tryWrapper {
        val response = client.get(EndPoints.MEDICINE)
        mapResponse(response.body())
    }

    override suspend fun getDiseases(): Result<List<com.example.model.app.Disease>> = tryWrapper {
        val response = client.get(EndPoints.DISEASE)
        mapResponse(response.body())
    }

    override suspend fun getSymptoms(): Result<List<com.example.model.app.Symptom>> = tryWrapper {
        val response = client.get(EndPoints.SYMPTOM)
        mapResponse(response.body())
    }

    override suspend fun getUserTransactions(id: String): Result<List<com.example.model.app.TransactionView>> = tryWrapper {
        val response = client.get(EndPoints.getUserTransactions(id))
        mapResponse(response.body())
    }

    override suspend fun getDonnerUser(id: String): Result<com.example.model.app.User.Donor> = tryWrapper {
        val response = client.get(EndPoints.getUser(id))
        mapResponse(response.body())
    }

    override suspend fun getReceiverUser(id: String): Result<com.example.model.app.User.Receiver> = tryWrapper {
        val response = client.get(EndPoints.getUser(id))
        mapResponse(response.body())
    }

    private fun <T> mapResponse(response: RemoteResponse<T>): Result<T> {
        return if (response.isSuccess)
            Result.Success(response.data!!)
        else
            Result.Error(response.error)
    }
}