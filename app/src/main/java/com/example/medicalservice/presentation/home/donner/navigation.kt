package com.example.medicalservice.presentation.home.donner

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.medicalservice.presentation.home.navigation.HomeScreenRoute

fun NavGraphBuilder.donnerHome(
    onMedicineClick: (String) -> Unit,
    onDonateClick: () -> Unit
) {
    composable(HomeScreenRoute) {
        DonnerHomeScreen(
            onMedicineClick = onMedicineClick,
            onDonateClick = onDonateClick
        )
    }
}