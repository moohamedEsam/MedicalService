package com.example.medicalservice.presentation.diagnosisResult.form

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.diagnosisResultForm(){
    composable(Destination.DiagnosisResultForm.fullRoute){
        val id = it.arguments?.getString(Destination.DiagnosisResultForm.diagnosisResultIdKey)?:""
        DiagnosisResultFormScreen(diagnosisResultId = id)
    }
}