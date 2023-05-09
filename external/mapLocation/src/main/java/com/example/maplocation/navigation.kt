package com.example.maplocation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.mapScreen() {
    composable(Destination.Map.route) {
        MapScreen()
    }
}