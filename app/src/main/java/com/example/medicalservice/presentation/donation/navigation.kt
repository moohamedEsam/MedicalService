package com.example.medicalservice.presentation.donation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val DonationScreenRoute = "donation"

fun NavGraphBuilder.donationScreen(
    onNavigateToMedicineScreen: (String) -> Unit,
) {
    composable(
        route = "$DonationScreenRoute/{donationRequestId}",
        arguments = listOf(
            navArgument("donationRequestId") {
                type = NavType.StringType
            }
        )
    ) {
        val donationRequestId = it.arguments?.getString("donationRequestId") ?: " "
        DonationScreen(
            donationRequestId = donationRequestId,
            onMedicineReadMoreClick = onNavigateToMedicineScreen
        )
    }
}

fun NavHostController.navigateToDonationScreen(donationRequestId: String? = " ") {
    navigate("$DonationScreenRoute/$donationRequestId")
}