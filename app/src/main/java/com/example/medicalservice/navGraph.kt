package com.example.medicalservice

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import com.example.medicalservice.presentation.diseasePrediction.diseasePrediction
import com.example.medicalservice.presentation.donation.donationScreen
import com.example.medicalservice.presentation.donation.navigateToDonationScreen
import com.example.medicalservice.presentation.donationList.donationListScreen
import com.example.medicalservice.presentation.home.navigation.homeScreen
import com.example.medicalservice.presentation.home.navigation.navigateToHomeScreen
import com.example.medicalservice.presentation.medicine.medicineDetailsScreen
import com.example.medicalservice.presentation.medicine.navigateToMedicineDetailsScreen
import com.example.model.app.UserType

@Composable
fun MedicalServiceNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    userType: UserType = UserType.Donner,
    paddingValues: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier.padding(paddingValues)
    ) {
        loginScreen(
            logo = R.drawable.meidcal_service,
            onLoggedIn = navHostController::navigateToHomeScreen,
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

        homeScreen(
            userType = userType,
            onNavigateToMedicineScreen = navHostController::navigateToMedicineDetailsScreen,
            onNavigateToDonateScreen = navHostController::navigateToDonationScreen,
            onNavigateToDiseaseScreen = navHostController::navigateToDiseaseScreen,
            onNavigateToTransactionScreen = {}
        )

        donationScreen(
            onNavigateToMedicineScreen = navHostController::navigateToMedicineDetailsScreen
        )

        diseasePrediction(
            onMedicineClick = navHostController::navigateToMedicineDetailsScreen,
            onDiseaseClick = navHostController::navigateToDiseaseScreen
        )

        donationListScreen()
    }
}


