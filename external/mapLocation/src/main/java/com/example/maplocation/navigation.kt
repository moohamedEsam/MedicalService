package com.example.maplocation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.mapScreen() {
    composable(Destination.Map.fullRoute) {
        val lat = it.arguments?.getString(Destination.Map.latKey)?.toDouble() ?: 0.0
        val lng = it.arguments?.getString(Destination.Map.lngKey)?.toDouble() ?: 0.0
        MapScreen(lat, lng)
    }
}