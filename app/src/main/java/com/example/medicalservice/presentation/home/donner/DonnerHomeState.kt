package com.example.medicalservice.presentation.home.donner

import com.example.models.app.DonationRequest
import com.example.models.app.Transaction
import com.example.models.app.User
import com.example.models.app.emptyDonor

data class DonnerHomeState(
    val user: User.Donor = User.emptyDonor(),
    val donationRequests: List<DonationRequest> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val query: String = "",
)
