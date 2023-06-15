package com.example.network

import com.example.common.functions.tryWrapper
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
import com.example.model.app.user.CreateUserDto
import com.example.model.app.user.User
import com.example.network.models.NetworkDiagnosisRequest
import com.example.network.models.NetworkTransaction
import com.example.network.models.NetworkUser
import com.example.network.models.NetworkDiagnosisResult
import com.example.network.models.RemoteResponse
import com.example.network.models.asDomainModel
import com.example.network.models.asNetworkModel
import com.example.network.models.toDiagnosisResult
import com.example.network.models.toNetwork
import com.example.network.models.toNetworkCreateUserDto
import com.example.network.models.toUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.koin.core.annotation.Single

@Single(binds = [RemoteDataSource::class])
class KtorRemoteDataSource(
    private val client: HttpClient
) : RemoteDataSource {
    override suspend fun login(credentials: Credentials): Result<Token> =
        tryWrapper {
            val response = client.post(EndPoints.login()) {
                setBody(credentials)
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK)
                Result.Success(response.body())
            else
                Result.Error("wrong username or password")
        }

    override suspend fun logout() {
        val authProvider =
            client.plugin(Auth).providers.firstOrNull() as? BearerAuthProvider ?: return
        authProvider.clearToken()
    }

    override suspend fun register(createUserDto: CreateUserDto): Result<Unit> =
        tryWrapper {
            val response = client.post(EndPoints.register()) {
                setBody(createUserDto.toNetworkCreateUserDto())
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun getDonationRequests(): Result<List<DonationRequest>> =
        tryWrapper {
            val response = client.get(EndPoints.donationRequest())
            mapResponse(response.body())
        }

    override suspend fun getMedicines(): Result<List<Medicine>> = tryWrapper {
        val response = client.get(EndPoints.medicine())
        mapResponse(response.body())
    }

    override suspend fun getDiseases(): Result<List<Disease>> = tryWrapper {
        val response = client.get(EndPoints.disease())
        mapResponse(response.body())
    }

    override suspend fun createDisease(disease: Disease): Result<Disease> {
        val response = client.post(EndPoints.createDisease()) {
            setBody(disease)
            contentType(ContentType.Application.Json)
        }
        return mapResponse(response.body())
    }

    override suspend fun createMedicine(medicine: Medicine): Result<Medicine> {
        val response = client.post(EndPoints.createMedicine()) {
            setBody(medicine)
            contentType(ContentType.Application.Json)
        }
        return mapResponse(response.body())
    }

    override suspend fun getSymptoms(): Result<List<Symptom>> = tryWrapper {
        val response = client.get(EndPoints.symptom())
        if (response.status.isSuccess())
            Result.Success(response.body<List<String>>().map { Symptom(it) })
        else
            Result.Error("error")
    }

    override suspend fun predictDiseaseBySymptoms(symptoms: List<Symptom>): Result<DiseaseView> =
        tryWrapper {
            val response = client.post(EndPoints.predictDisease()) {
                setBody(symptoms)
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun getUserTransactions(): Result<List<Transaction>> =
        tryWrapper {
            val response = client.get(EndPoints.getUserTransactions())
            val result = mapResponse(response.body<RemoteResponse<List<NetworkTransaction>>>())
            result.map { it.map { transaction -> transaction.asDomainModel() } }
        }

    override suspend fun getTransactions(): Result<List<Transaction>> = tryWrapper {
        val response = client.get(EndPoints.getTransactions())
        val result = mapResponse(response.body<RemoteResponse<List<NetworkTransaction>>>())
        result.map { it.map { transaction -> transaction.asDomainModel() } }
    }

    override suspend fun createTransaction(transaction: Transaction): Result<Transaction> =
        tryWrapper {
            val response = client.post(EndPoints.createTransaction()) {
                setBody(transaction.asNetworkModel())
                contentType(ContentType.Application.Json)
            }
            mapResponse<NetworkTransaction>(response.body()).map { it.asDomainModel() }
        }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> = tryWrapper {
        val response = client.put(EndPoints.updateTransaction(transaction.id)) {
            setBody(transaction.asNetworkModel())
            contentType(ContentType.Application.Json)
        }
        mapResponse(response.body())
    }

    override suspend fun getAllUsers(): Result<List<User>> = tryWrapper {
        val response = client.get(EndPoints.getUsers())
        when (val result = mapResponse(response.body<RemoteResponse<List<NetworkUser>>>())) {
            is Result.Error -> return@tryWrapper Result.Error(result.exception)
            is Result.Success -> Result.Success(result.data.map { it.toUser() })
            else -> Result.Error("unknown error")
        }
    }

    override suspend fun getCurrentUserDiagnosisRequests(): Result<List<DiagnosisRequest>> =
        tryWrapper {
            val response = client.get(EndPoints.getDiagnosisRequests())
            val result = mapResponse<List<NetworkDiagnosisRequest>>(response.body())
            result.map { it.map { networkDiagnosisRequest -> networkDiagnosisRequest.toDiagnosisResult() } }
        }

    override suspend fun getCurrentUserDiagnosisResults(): Result<List<DiagnosisResult>> =
        tryWrapper {
            val response = client.get(EndPoints.getCurrentUserDiagnosisResult())
            val result = mapResponse<List<NetworkDiagnosisResult>>(response.body())
            result.map { it.map { networkDiagnosisResult -> networkDiagnosisResult.toDiagnosisResult() } }
        }

    override suspend fun createDiagnosisRequest(donationRequest: DiagnosisRequest): Result<Unit> =
        tryWrapper {
            val response = client.post(EndPoints.createDiagnosisRequest()) {
                setBody(donationRequest.toNetwork())
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun updateDiagnosisRequest(donationRequest: DiagnosisRequest): Result<Unit> =
        tryWrapper {
            val response = client.put(EndPoints.updateDiagnosisRequest(donationRequest.id)) {
                setBody(donationRequest.toNetwork())
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun createDiagnosisResult(donationResult: DiagnosisResult): Result<Unit> =
        tryWrapper {
            val response = client.post(EndPoints.createDiagnosisResult()) {
                setBody(donationResult.toNetwork())
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun updateDiagnosisResult(donationResult: DiagnosisResult): Result<DiagnosisResult> =
        tryWrapper {
            val response = client.put(EndPoints.updateDiagnosisResult(donationResult.id)) {
                setBody(donationResult.toNetwork())
                contentType(ContentType.Application.Json)
            }
            mapResponse<NetworkDiagnosisResult>(response.body()).map { it.toDiagnosisResult() }
        }

    private fun <T> mapResponse(response: RemoteResponse<T>): Result<T> {
        return if (response.isSuccess)
            Result.Success(response.data!!)
        else
            Result.Error(response.error)
    }
}