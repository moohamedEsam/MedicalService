package com.example.medicalservice.presentation.home.receiver

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.medicalservice.presentation.home.navigation.HomeScreenRoute


fun NavGraphBuilder.receiverHome(
    onNavigateToMedicineScreen: (String) -> Unit,
    onNavigateToDiseaseScreen: (String) -> Unit,
){
    composable(HomeScreenRoute){
        ReceiverHomeScreen(
            onDiseaseClick = onNavigateToDiseaseScreen,
            onMedicineClick = onNavigateToMedicineScreen
        )
    }
}