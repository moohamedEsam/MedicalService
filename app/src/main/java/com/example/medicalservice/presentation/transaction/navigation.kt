package com.example.medicalservice.presentation.transaction

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.Destination

fun NavGraphBuilder.transactionScreen() {
    composable(Destination.TransactionDetails.fullRoute) {
        val id = it.arguments?.getString(Destination.TransactionDetails.transactionIdKey) ?: ""
        TransactionDetailsScreen(id)
    }
}