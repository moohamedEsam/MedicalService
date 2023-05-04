package com.example.medicalservice.presentation.home.donner

import com.example.model.app.emptyDonor

data class DonnerHomeState(
    val user: com.example.model.app.User.Donor = com.example.model.app.User.emptyDonor(),
    val donationRequestViews: List<com.example.model.app.DonationRequestView> = emptyList(),
    val transactionViews: List<com.example.model.app.TransactionView> = emptyList(),
    val query: String = "",
)
