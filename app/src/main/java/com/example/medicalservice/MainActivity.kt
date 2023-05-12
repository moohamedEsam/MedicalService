package com.example.medicalservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.common.functions.SharedPrefsKey
import com.example.common.functions.loadFromSharedPrefs
import com.example.common.functions.loadToken
import com.example.common.navigation.Destination
import com.example.medicalservice.presentation.layout.MedicalServiceLayout
import com.example.medicalservice.ui.theme.MedicalServiceTheme
import com.example.model.app.user.UserType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val alreadyLoggedIn: Boolean = loadToken(this) != null
        val userType = UserType.valueOf(
            loadFromSharedPrefs(this, SharedPrefsKey.USER_TYPE) ?: UserType.Donner.name
        )
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MedicalServiceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MedicalServiceLayout(
                        startDestination = if (alreadyLoggedIn) Destination.Home.fullRoute
                        else Destination.Login.fullRoute,
                        userType = userType
                    )
                }
            }
        }
    }
}