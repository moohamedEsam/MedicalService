package com.example.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class NetworkModule {
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

        install(Auth){
            bearer {
                loadTokens {
                    BearerTokens("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb2hhbWVkRXNhbUBnbWFpbC5jb20iLCJjcmVhdGVkIjoxNjgzNDU4MDgyMzI0LCJleHAiOjE2ODQwNjI4ODJ9.nId8JUCVQM7gSu2v59mZHWcEUxbiSvquQkHhlcZmae4rewiUmEDJOG7ATJx4lt4vojJRxUZE5WPKgS6JMxvL1A", "")
                }
            }
        }
    }
}