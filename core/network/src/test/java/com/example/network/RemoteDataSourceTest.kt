package com.example.network

import com.example.common.models.Result
import com.example.model.app.auth.Credentials
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.empty
import com.example.model.app.transaction.Transaction
import com.example.model.app.transaction.empty
import com.example.model.app.user.CreateUserDto
import com.example.model.app.user.Location
import com.example.model.app.user.UserType
import com.example.network.models.toNetwork
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDataSourceTest {
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("log: $message")
                    }
                }
            }

            install(Auth) {
                val token =
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb2hhbWVkRXNhbUBnbWFpbC5jb20iLCJjcmVhdGVkIjoxNjg0MzA5MzY3MDY0LCJleHAiOjE2ODQ5MTQxNjd9.qJsxYyBv-0uWyamLfZzA7ndwjFDrigguGd9TqYIRpbUMl2YD_2PC4VkSCoMKDeeDrNlZbOBQG3FvgzsS5J6WZA"
                bearer {
                    loadTokens {
                        BearerTokens(token, token)
                    }
                    refreshTokens {
                        BearerTokens(token, token)
                    }
                    sendWithoutRequest {
                        !it.url.pathSegments.contains("login") && !it.url.pathSegments.contains("adduser")
                    }
                }
            }
        }
        remoteDataSource = KtorRemoteDataSource(client)
    }

    @Test
    fun `test login success`() = runTest {
        // arrange
        val email = "mohamedEsam@gmail.com"
        val password = "somethingRandom"

        // act
        val response = remoteDataSource.login(Credentials(email, password))

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
        response.ifSuccess { token ->
            assertThat(token).isNotNull()
        }
    }

    @Test
    fun `test login failure`() = runTest {
        // arrange
        val email = ""
        val password = ""

        // act
        val response = remoteDataSource.login(Credentials(email, password))

        // assert
        assertThat(response).isInstanceOf(Result.Error::class.java)
        response.ifFailure { error ->
            assertThat(error).isNotNull()
        }
    }

    @Test
    fun `test register email already exist should be failure`() = runTest {
        // arrange
        val createUserDto = CreateUserDto(
            username = "mohamed esam",
            email = "mohamedEsam@gmail.com",
            password = "123456a",
            phone = "01111111111",
            type = UserType.Receiver,
            location = Location(0.0, 0.0),
        )

        // act
        val response = remoteDataSource.register(createUserDto)

        // assert
        assertThat(response).isInstanceOf(Result.Error::class.java)
        response.ifFailure { error ->
            assertThat(error).isNotNull()
        }

    }

    @Test
    fun `test register success`() = runTest {
        // arrange
        val createUserDto = CreateUserDto(
            username = "mohamed esam",
            email = "mohamedEsam_receiver${UUID.randomUUID()}@gmail.com",
            password = "123456a",
            phone = "01111111111",
            type = UserType.Receiver,
            location = Location(0.0, 0.0),
        )

        // act
        val response = remoteDataSource.register(createUserDto)

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getDonationRequests() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getDonationRequests()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getMedicines() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getMedicines()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getDiseases() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getDiseases()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getSymptoms() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getSymptoms()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getUserTransactions() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getUserTransactions()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getTransactions() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getTransactions()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun createTransaction() = runTest {
        // arrange
        val transaction = Transaction.empty()

        // act
        val response = remoteDataSource.createTransaction(transaction)

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun updateTransaction() = runTest {
        // arrange
        val transaction = Transaction.empty()

        // act
        remoteDataSource.createTransaction(transaction)
        val response = remoteDataSource.updateTransaction(transaction)

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getAllUsers() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getAllUsers()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getCurrentUserDiagnosisRequests() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getCurrentUserDiagnosisRequests()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun getCurrentUserDiagnosisResults() = runTest {
        // arrange

        // act
        val response = remoteDataSource.getCurrentUserDiagnosisResults()

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun createDiagnosisRequest() = runTest {
        // arrange
        val diagnosisRequest = DiagnosisRequest.empty()

        // act
        val response = remoteDataSource.createDiagnosisRequest(diagnosisRequest)

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun updateDiagnosisRequest() = runTest {
        // arrange
        val diagnosisRequest = DiagnosisRequest.empty()

        // act
        remoteDataSource.createDiagnosisRequest(diagnosisRequest)
        val response = remoteDataSource.updateDiagnosisRequest(diagnosisRequest)

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun createDiagnosisResult() = runTest {
        // arrange
        val diagnosisResult = DiagnosisResult.empty()
        // act
        val response = remoteDataSource.createDiagnosisResult(diagnosisResult)

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun updateDiagnosisResult() = runTest {
        // arrange
        val diagnosisResult = DiagnosisResult.empty()

        // act
        remoteDataSource.createDiagnosisResult(diagnosisResult)
        val response = remoteDataSource.updateDiagnosisResult(diagnosisResult)

        // assert
        assertThat(response).isInstanceOf(Result.Success::class.java)
    }
}