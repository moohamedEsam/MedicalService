package com.example.medicalservice.presentation.home.receiver

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val ReceiverHomeRoute = "Home/Receiver"

fun NavGraphBuilder.receiverHome(
    onDiseaseClick: (String) -> Unit,
    onMedicineClick: (String) -> Unit
){
    composable(ReceiverHomeRoute){
        ReceiverHomeScreen(
            onDiseaseClick = onDiseaseClick,
            onMedicineClick = onMedicineClick
        )
    }
}

fun NavHostController.navigateToReceiverHomeScreen(){
    navigate(ReceiverHomeRoute){
        launchSingleTop = true
    }
}