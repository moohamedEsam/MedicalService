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
import com.example.medicalservice.presentation.diagnosisResult.details.diagnosisDetailsScreen
import com.example.medicalservice.presentation.diagnosisRequest.form.diagnosisFormScreen
import com.example.medicalservice.presentation.diagnosisResult.form.diagnosisResultForm
import com.example.medicalservice.presentation.disease.diseaseScreen
import com.example.medicalservice.presentation.donation.donationScreen
import com.example.medicalservice.presentation.donationList.donationListScreen
import com.example.medicalservice.presentation.home.navigation.homeScreen
import com.example.medicalservice.presentation.medicine.medicineDetailsScreen
import com.example.medicalservice.presentation.myDonations.myDonationsScreen
import com.example.medicalservice.presentation.settings.settingsScreen
import com.example.medicalservice.presentation.transaction.transactionScreen
import com.example.medicalservice.presentation.uploadPrescription.uploadPrescriptionScreen

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
        diagnosisFormScreen()
        diagnosisDetailsScreen()
        donationScreen()
        donationListScreen()
        myDonationsScreen()
        uploadPrescriptionScreen()
        settingsScreen()
        diagnosisResultForm()
    }
}


