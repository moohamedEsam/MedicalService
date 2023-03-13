package com.example.auth.register

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.maplocation.latKey
import com.example.maplocation.lngKey

const val RegisterScreenRoute = "RegisterScreen"

fun NavHostController.navigateToRegisterScreen() {
    navigate(RegisterScreenRoute)
}

fun NavGraphBuilder.registerScreen(
    logo:Any,
    onRegistered: () -> Unit,
    onLoginClick: () -> Unit,
    onLocationRequested: () -> Unit
) {
    composable(RegisterScreenRoute) {
        val lat = it.arguments?.getDouble(latKey) ?: 0.0
        val lng = it.arguments?.getDouble(lngKey) ?: 0.0
        RegisterScreen(
            onRegistered = onRegistered,
            onLoginClick = onLoginClick,
            onLocationRequested = onLocationRequested,
            logo = logo,
            lat = lat,
            lng = lng
        )
    }
}