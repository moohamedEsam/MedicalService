package com.example.medicalservice.presentation.donation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.donationScreen() {
    composable(
        route = Destination.DonationDetails.fullRoute,
    ) {
        val donationRequestId = it.arguments?.getString(Destination.DonationDetails.donationIdKey) ?: " "
        DonationScreen(donationRequestId = donationRequestId)
    }
}