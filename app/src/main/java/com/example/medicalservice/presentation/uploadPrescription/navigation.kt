package com.example.medicalservice.presentation.uploadPrescription

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.uploadPrescriptionScreen() {
    composable(Destination.UploadPrescription.fullRoute) {
        UploadPrescriptionScreen()
    }
}