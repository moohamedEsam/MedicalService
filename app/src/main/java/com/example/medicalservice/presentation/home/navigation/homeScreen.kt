package com.example.medicalservice.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.medicalservice.presentation.home.donner.donnerHome
import com.example.medicalservice.presentation.home.receiver.receiverHome
import com.example.models.auth.UserType

const val HomeScreenRoute = "Home"

fun NavGraphBuilder.homeScreen(
    userType: UserType,
    onMedicineClick: (String) -> Unit,
    onDonateClick: () -> Unit,
    onDiseaseClick: (String) -> Unit
) {
    if (userType == UserType.Donner)
        donnerHome(
            onMedicineClick = onMedicineClick,
            onDonateClick = onDonateClick
        )
    else
        receiverHome(
            onDiseaseClick = onDiseaseClick,
            onMedicineClick = onMedicineClick
        )
}

fun NavHostController.navigateToHomeScreen() {
    navigate(HomeScreenRoute) {
        launchSingleTop = true
    }
}