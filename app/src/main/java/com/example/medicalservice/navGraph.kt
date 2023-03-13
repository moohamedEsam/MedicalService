package com.example.medicalservice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.auth.login.loginScreen
import com.example.auth.register.navigateToRegisterScreen
import com.example.auth.register.registerScreen
import com.example.maplocation.latKey
import com.example.maplocation.lngKey
import com.example.maplocation.mapScreen
import com.example.maplocation.navigateToMapScreen
import com.example.medicalservice.presentation.disease.diseaseScreen
import com.example.medicalservice.presentation.disease.navigateToDiseaseScreen
import com.example.medicalservice.presentation.donation.donationScreen
import com.example.medicalservice.presentation.donation.navigateToDonationScreen
import com.example.medicalservice.presentation.home.donner.donnerHome
import com.example.medicalservice.presentation.home.receiver.receiverHome
import com.example.medicalservice.presentation.medicine.medicineDetailsScreen
import com.example.medicalservice.presentation.medicine.navigateToMedicineDetailsScreen

@Composable
fun MedicalServiceNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen(
            logo = R.drawable.meidcal_service,
            onLoggedIn = {},
            onRegisterClick = navHostController::navigateToRegisterScreen
        )

        mapScreen { lat, lng ->
            navHostController.previousBackStackEntry?.arguments?.putDouble(latKey, lat)
            navHostController.previousBackStackEntry?.arguments?.putDouble(lngKey, lng)
            navHostController.popBackStack()
        }

        registerScreen(
            logo = R.drawable.meidcal_service,
            onRegistered = {},
            onLoginClick = navHostController::popBackStack,
            onLocationRequested = navHostController::navigateToMapScreen
        )

        medicineDetailsScreen(
            onDiseaseClick = navHostController::navigateToDiseaseScreen
        )

        diseaseScreen(
            onMedicineClick = navHostController::navigateToMedicineDetailsScreen
        )

        receiverHome(
            onDiseaseClick = navHostController::navigateToDiseaseScreen,
            onMedicineClick = navHostController::navigateToMedicineDetailsScreen
        )

        donnerHome(
            onMedicineClick = navHostController::navigateToMedicineDetailsScreen,
            onDonateClick = navHostController::navigateToDonationScreen
        )

        donationScreen()
    }
}