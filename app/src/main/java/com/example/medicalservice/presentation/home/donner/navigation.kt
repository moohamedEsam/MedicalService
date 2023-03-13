package com.example.medicalservice.presentation.home.donner

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val DonnerHomeScreenRoute = "Home/Donner"

fun NavGraphBuilder.donnerHome(
    onMedicineClick: (String) -> Unit,
    onDonateClick: () -> Unit
) {
    composable(DonnerHomeScreenRoute) {
        DonnerHomeScreen(
            onMedicineClick = onMedicineClick,
            onDonateClick = onDonateClick
        )
    }
}

fun NavHostController.navigateToDonnerHomeScreen() {
    navigate(DonnerHomeScreenRoute){
        launchSingleTop = true
    }
}