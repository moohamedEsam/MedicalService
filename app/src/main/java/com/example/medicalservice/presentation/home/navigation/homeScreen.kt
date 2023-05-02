package com.example.medicalservice.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.medicalservice.presentation.home.donner.donnerHome
import com.example.medicalservice.presentation.home.receiver.receiverHome
import com.example.models.auth.UserType

const val HomeScreenRoute = "Home"

fun NavGraphBuilder.homeScreen(
    userType: UserType,
    onNavigateToMedicineScreen: (String) -> Unit,
    onNavigateToDiseaseScreen: (String) -> Unit,
    onNavigateToDonateScreen: (String?) -> Unit,
    onNavigateToTransactionScreen: (String) -> Unit,
) {
    if (userType == UserType.Donner)
        donnerHome(
            onNavigateToMedicineScreen = onNavigateToMedicineScreen,
            onNavigateToDonateScreen = onNavigateToDonateScreen,
            onNavigateToTransactionScreen = onNavigateToTransactionScreen
        )
    else
        receiverHome(
            onNavigateToMedicineScreen = onNavigateToMedicineScreen,
            onNavigateToDiseaseScreen = onNavigateToDiseaseScreen
        )
}

fun NavHostController.navigateToHomeScreen() {
    navigate(HomeScreenRoute) {
        launchSingleTop = true
    }
}