package com.example.medicalservice.presentation.diagnosisResult.details

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.diagnosisDetailsScreen() {
    composable(Destination.DiagnosisDetails.fullRoute) {
        val id = it.arguments?.getString(Destination.DiagnosisDetails.diagnosisIdKey) ?: ""
        DiagnosisDetailsScreen(id)
    }
}