package com.example.medicalservice.presentation.myDonations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.myDonationsScreen(){
    composable(Destination.MyDonationsList.fullRoute){
        MyDonationsScreen()
    }
}