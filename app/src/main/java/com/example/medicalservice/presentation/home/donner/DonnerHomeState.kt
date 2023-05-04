package com.example.medicalservice.presentation.home.donner

import com.example.model.app.DonationRequest
import com.example.model.app.Transaction
import com.example.model.app.User
import com.example.model.app.emptyDonor

data class DonnerHomeState(
    val user: com.example.model.app.User.Donor = com.example.model.app.User.emptyDonor(),
    val donationRequests: List<com.example.model.app.DonationRequest> = emptyList(),
    val transactions: List<com.example.model.app.Transaction> = emptyList(),
    val query: String = "",
)
