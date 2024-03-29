package com.example.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

object UserSettingsSerializer : Serializer<UserSettings>{
    override val defaultValue: UserSettings
        get() = UserSettings()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): UserSettings {
        return try {
            Json.decodeFromStream(input)
        } catch (e: SerializationException){
            e.printStackTrace()
            defaultValue
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        Json.encodeToStream(t, output)
    }
}