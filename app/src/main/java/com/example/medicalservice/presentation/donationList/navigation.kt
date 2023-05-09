package com.example.medicalservice.presentation.donationList

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.donationListScreen() {
    composable(Destination.DonationsList.fullRoute) {
        DonationListScreen()
    }
}