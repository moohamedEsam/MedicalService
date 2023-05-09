package com.example.auth.register

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.registerScreen(logo: Any) {
    composable(Destination.Register().fullRoute) {
        val lat = it.arguments?.getString(Destination.Map.latKey)?.toDoubleOrNull() ?: 0.0
        val lng = it.arguments?.getString(Destination.Map.lngKey)?.toDoubleOrNull() ?: 0.0
        RegisterScreen(
            logo = logo,
            lat = lat,
            lng = lng
        )
    }
}
