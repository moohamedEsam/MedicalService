package com.example.medicalservice.presentation.home.receiver

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination


fun NavGraphBuilder.receiverHome(){
    composable(Destination.Home.fullRoute){
        ReceiverHomeScreen()
    }
}