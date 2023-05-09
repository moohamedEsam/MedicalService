package com.example.medicalservice.presentation.medicine

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.medicineDetailsScreen() {
    composable(
        route = Destination.MedicineDetails.fullRoute,
    ) { backStackEntry ->
        val medicineId = backStackEntry.arguments?.getString(Destination.MedicineDetails.medicineIdKey) ?: return@composable
        MedicineDetailsScreen(medicineId = medicineId)
    }
}