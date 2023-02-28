package com.example.auth.register

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

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
        RegisterScreen(
            onRegistered = onRegistered,
            onLoginClick = onLoginClick,
            onLocationRequested = onLocationRequested,
            logo = logo
        )
    }
}
