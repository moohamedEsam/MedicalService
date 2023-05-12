package com.example.medicalservice

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.auth.login.loginScreen
import com.example.auth.register.registerScreen
import com.example.maplocation.mapScreen
import com.example.medicalservice.presentation.disease.diseaseScreen
import com.example.medicalservice.presentation.donation.donationScreen
import com.example.medicalservice.presentation.donationList.donationListScreen
import com.example.medicalservice.presentation.home.navigation.homeScreen
import com.example.medicalservice.presentation.medicine.medicineDetailsScreen
import com.example.medicalservice.presentation.myDonations.myDonationsScreen
import com.example.medicalservice.presentation.transaction.transactionScreen

@Composable
fun MedicalServiceNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier.padding(paddingValues)
    ) {
        loginScreen(logo = R.drawable.meidcal_service)
        registerScreen(logo = R.drawable.meidcal_service)
        mapScreen()
        medicineDetailsScreen()
        diseaseScreen()
        homeScreen()
        transactionScreen()
        donationScreen()
        donationListScreen()
        myDonationsScreen()
    }
}


