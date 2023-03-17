package com.example.medicalservice.presentation.diseasePrediction

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val DiseasePredictionScreenRoute = "Disease Prediction"

fun NavGraphBuilder.diseasePrediction(
    onDiseaseClick: (String) -> Unit,
    onMedicineClick: (String) -> Unit
) {
    composable(DiseasePredictionScreenRoute) {
        DiseasePredictionScreen(
            onDiseaseClick = onDiseaseClick,
            onMedicineClick = onMedicineClick
        )
    }
}


fun NavHostController.navigateToDiseasePredictionScreen() {
    navigate(DiseasePredictionScreenRoute){
        launchSingleTop = true
    }
}