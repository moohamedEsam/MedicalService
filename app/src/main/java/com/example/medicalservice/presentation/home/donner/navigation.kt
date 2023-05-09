package com.example.medicalservice.presentation.home.donner

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.donnerHome() {
    composable(Destination.Home.fullRoute) {
        DonnerHomeScreen()
    }
}