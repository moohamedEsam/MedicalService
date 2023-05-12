package com.example.datastore

import android.content.Context
import androidx.datastore.dataStore

val Context.dataStore by dataStore(
    fileName = "user_settings.json",
    serializer = UserSettingsSerializer
)