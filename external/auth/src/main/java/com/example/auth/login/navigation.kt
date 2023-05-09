package com.example.auth.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination


fun NavGraphBuilder.loginScreen(
    logo: Any
) {
    composable(Destination.Login.fullRoute) {
        LoginScreen(logo = logo)
    }
}