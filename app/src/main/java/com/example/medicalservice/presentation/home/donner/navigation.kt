package com.example.medicalservice.presentation.home.donner

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.medicalservice.presentation.home.navigation.HomeScreenRoute

fun NavGraphBuilder.donnerHome(
    onNavigateToMedicineScreen: (String) -> Unit,
    onNavigateToDonateScreen: (String?) -> Unit,
    onNavigateToTransactionScreen: (String) -> Unit
) {
    composable(HomeScreenRoute) {
        DonnerHomeScreen(
            onMedicineClick = onNavigateToMedicineScreen,
            onDonateClick = onNavigateToDonateScreen,
            onTransactionClick = onNavigateToTransactionScreen
        )
    }
}