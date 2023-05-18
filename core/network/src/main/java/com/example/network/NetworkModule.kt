package com.example.network

import android.util.Log
import com.example.common.functions.loadToken
import com.example.datastore.dataStore
import com.example.model.app.auth.Credentials
import com.example.model.app.auth.Token
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
@ComponentScan
class NetworkModule {
    context(Scope)
            @Single
    fun provideHttpClient() = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i("ktor", "log: $message")
                }
            }
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = androidContext().loadToken() ?: ""
                    BearerTokens(token, token)
                }
                refreshTokens {
                    val token = androidContext().loadToken() ?: ""
                    BearerTokens(token, token)
                }
                sendWithoutRequest {
                    !it.url.pathSegments.contains("login") && !it.url.pathSegments.contains("adduser")
                }
            }
        }
    }

    companion object{
        fun getTestClient() = HttpClient(CIO) {
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
    }
}