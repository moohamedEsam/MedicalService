package com.example.medicalservice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.auth.login.loginScreen
import com.example.auth.register.navigateToRegisterScreen
import com.example.auth.register.registerScreen
import com.example.maplocation.latKey
import com.example.maplocation.lngKey
import com.example.maplocation.mapScreen
import com.example.maplocation.navigateToMapScreen

@Composable
fun MedicalServiceNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen(
            logo = R.drawable.meidcal_service,
            onLoggedIn = {},
            onRegisterClick = navHostController::navigateToRegisterScreen
        )

        mapScreen { lat, lng ->
            navHostController.previousBackStackEntry?.arguments?.putDouble(latKey, lat)
            navHostController.previousBackStackEntry?.arguments?.putDouble(lngKey, lng)
            navHostController.popBackStack()
        }

        registerScreen(
            logo = R.drawable.meidcal_service,
            onRegistered = {},
            onLoginClick = navHostController::popBackStack,
            onLocationRequested = navHostController::navigateToMapScreen
        )
    }
}