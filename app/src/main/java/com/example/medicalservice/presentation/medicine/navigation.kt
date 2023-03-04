package com.example.medicalservice.presentation.medicine

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val medicineDetailsScreen = "Medicine Details"

fun NavGraphBuilder.medicineDetailsScreen(
    onDiseaseClick: (String) -> Unit
) {
    composable(
        route = "$medicineDetailsScreen/{medicineId}",
    ) { backStackEntry ->
        val medicineId = backStackEntry.arguments?.getString("medicineId") ?: return@composable
        MedicineDetailsScreen(medicineId = medicineId, onDiseaseClick = onDiseaseClick)
    }
}

fun NavHostController.navigateToMedicineDetailsScreen(medicineId: String) {
    navigate("$medicineDetailsScreen/$medicineId")
}