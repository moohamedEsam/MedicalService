package com.example.medicalservice.presentation.diagnosisRequest.form

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.diagnosisFormScreen() {
    composable(Destination.DiagnosisForm.fullRoute){
        DiagnosisFormScreen()
    }
}