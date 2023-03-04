package com.example.medicalservice.presentation.disease

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable


const val DiseaseScreen = "disease"

fun NavGraphBuilder.diseaseScreen(
    onMedicineClick: (String) -> Unit
) {
    composable(
        route = "$DiseaseScreen/{diseaseId}"
    ) { backStackEntry ->
        val diseaseId = backStackEntry.arguments?.getString("diseaseId") ?: return@composable
        DiseaseScreen(diseaseId = diseaseId, onMedicineClick = onMedicineClick)
    }
}

fun NavHostController.navigateToDiseaseScreen(diseaseId: String) {
    navigate("$DiseaseScreen/$diseaseId")
}