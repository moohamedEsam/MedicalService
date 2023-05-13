package com.example.medicalservice.presentation.diagnosis.form

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.diagnosisFormScreen() {
    composable(Destination.DiagnosisForm.fullRoute){
        DiagnosisFormScreen()
    }
}