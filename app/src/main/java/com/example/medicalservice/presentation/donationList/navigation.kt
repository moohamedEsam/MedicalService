package com.example.medicalservice.presentation.donationList

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val DonationListScreenRoute = "donationList"

fun NavGraphBuilder.donationListScreen() {
    composable(DonationListScreenRoute) {
        DonationListScreen()
    }
}

fun NavHostController.navigateToDonationListScreen() {
    navigate(DonationListScreenRoute)
}