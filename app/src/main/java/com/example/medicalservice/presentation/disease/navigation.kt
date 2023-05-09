package com.example.medicalservice.presentation.disease

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.diseaseScreen() {
    composable(
        route = Destination.DiseaseDetails.fullRoute
    ) { backStackEntry ->
        val diseaseId = backStackEntry.arguments?.getString(Destination.DiseaseDetails.diseaseIdKey) ?: return@composable
        DiseaseScreen(diseaseId = diseaseId)
    }
}