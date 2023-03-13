package com.example.medicalservice.presentation.donation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val DonationScreen = "donation"

fun NavGraphBuilder.donationScreen(
) {
    composable(
        route = DonationScreen,
    ) {
        DonationScreen()
    }
}

fun NavHostController.navigateToDonationScreen() {
    navigate(DonationScreen)
}