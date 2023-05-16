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
import com.example.model.app.user.Register
import com.example.model.app.user.User
import com.example.model.app.user.toUser
import com.example.network.models.NetworkTransaction
import com.example.network.models.RemoteResponse
import com.example.network.models.asDomainModel
import com.example.network.models.toNetworkRegister
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.koin.core.annotation.Single

@Single(binds = [RemoteDataSource::class])
class KtorRemoteDataSource(
    private val client: HttpClient
) : RemoteDataSource {
    override suspend fun login(credentials: Credentials): Result<Token> =
        tryWrapper {
            val response = client.post(EndPoints.LOGIN) {
                setBody(credentials)
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK)
                Result.Success(response.body())
            else
                Result.Error("wrong username or password")
        }

    override suspend fun register(register: Register): Result<Unit> =
        tryWrapper {
            val response = client.post(EndPoints.REGISTER) {
                setBody(register.toNetworkRegister())
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun getDonationRequests(): Result<List<DonationRequest>> =
        tryWrapper {
            val response = client.get(EndPoints.DONATION_REQUEST)
            mapResponse(response.body())
        }

    override suspend fun getMedicines(): Result<List<Medicine>> = tryWrapper {
        val response = client.get(EndPoints.MEDICINE)
        mapResponse(response.body())
    }

    override suspend fun getDiseases(): Result<List<Disease>> = tryWrapper {
        val response = client.get(EndPoints.DISEASE)
        mapResponse(response.body())
    }

    override suspend fun getSymptoms(): Result<List<Symptom>> = tryWrapper {
        val response = client.get(EndPoints.SYMPTOM)
        mapResponse(response.body())
    }

    override suspend fun predictDiseaseBySymptoms(symptoms: List<Symptom>): Result<DiseaseView> =
        tryWrapper {
            val response = client.post(EndPoints.PREDICT_DISEASE) {
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

    override suspend fun createTransaction(transaction: Transaction): Result<Unit> = tryWrapper {
        val response = client.post(EndPoints.createTransaction()) {
            setBody(transaction)
            contentType(ContentType.Application.Json)
        }
        mapResponse(response.body())
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> = tryWrapper {
        val response = client.post(EndPoints.updateTransaction(transaction.id)) {
            setBody(transaction)
            contentType(ContentType.Application.Json)
        }
        mapResponse(response.body())
    }

    override suspend fun getDonnerUser(id: String): Result<User.Donor> = tryWrapper {
        val response = client.get(EndPoints.getUser(id))
        mapResponse(response.body())
    }

    override suspend fun getCurrentUser(email: String): Result<User> = tryWrapper {
        val response = client.get(EndPoints.getCurrentUser())
        val result = mapResponse(response.body<RemoteResponse<List<User.Unknown>>>())
        if (result is Result.Error)
            return@tryWrapper Result.Error(result.exception)
        val unknownUser = (result as Result.Success).data.find { it.email == email }
            ?: return@tryWrapper Result.Error("user not found")

        Result.Success(unknownUser.toUser())
    }

    override suspend fun getAllUsers(): Result<List<User>> = tryWrapper {
        val response = client.get(EndPoints.getUsers())
        when (val result = mapResponse(response.body<RemoteResponse<List<User.Unknown>>>())) {
            is Result.Error -> return@tryWrapper Result.Error(result.exception)
            is Result.Success -> Result.Success(result.data.map { it.toUser() })
            else -> Result.Error("unknown error")
        }
    }

    override suspend fun getCurrentUserDiagnosisRequests(): Result<List<DiagnosisRequest>> =
        tryWrapper {
            val response = client.get(EndPoints.getDiagnosisRequests())
            mapResponse(response.body())
        }

    override suspend fun getCurrentUserDiagnosisResults(): Result<List<DiagnosisResult>> =
        tryWrapper {
            val response = client.get(EndPoints.getDiagnosisResults())
            mapResponse(response.body())
        }

    override suspend fun createDiagnosisRequest(donationRequest: DiagnosisRequest): Result<Unit> =
        tryWrapper {
            val response = client.post(EndPoints.createDiagnosisRequest()) {
                setBody(donationRequest)
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun updateDiagnosisRequest(donationRequest: DiagnosisRequest): Result<Unit> =
        tryWrapper {
            val response = client.post(EndPoints.updateDiagnosisRequest(donationRequest.id)) {
                setBody(donationRequest)
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun createDiagnosisResult(donationRequest: DiagnosisResult): Result<Unit> =
        tryWrapper {
            val response = client.post(EndPoints.createDiagnosisResult()) {
                setBody(donationRequest)
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun updateDiagnosisResult(donationRequest: DiagnosisResult): Result<Unit> =
        tryWrapper {
            val response = client.post(EndPoints.updateDiagnosisResult(donationRequest.id)) {
                setBody(donationRequest)
                contentType(ContentType.Application.Json)
            }
            mapResponse(response.body())
        }

    override suspend fun getReceiverUser(id: String): Result<User.Receiver> =
        tryWrapper {
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