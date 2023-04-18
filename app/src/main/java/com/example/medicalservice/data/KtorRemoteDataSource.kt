package com.example.medicalservice.data

import com.example.common.functions.tryWrapper
import com.example.common.models.Result
import com.example.medicalservice.data.models.RemoteResponse
import com.example.models.RemoteDataSource
import com.example.models.auth.Credentials
import com.example.models.auth.Token
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single

@Single(binds = [RemoteDataSource::class])
class KtorRemoteDataSource(
    private val client: HttpClient
) : RemoteDataSource {
    override suspend fun login(credentials: Credentials): Result<Token> = tryWrapper {
        val response = client.post(EndPoints.LOGIN) {
            setBody(credentials)
            contentType(ContentType.Application.Json)
        }
        mapResponse(response.body())
    }

    private fun <T> mapResponse(response: RemoteResponse<T>): Result<T> {
        return if (response.isSuccess)
            Result.Success(response.data!!)
        else
            Result.Error(response.error)
    }
}