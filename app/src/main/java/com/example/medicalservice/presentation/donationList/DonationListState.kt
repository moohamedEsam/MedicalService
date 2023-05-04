package com.example.medicalservice.presentation.donationList

import com.example.model.app.DonationRequest

data class DonationListState(
    val donationRequests: List<com.example.model.app.DonationRequest> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
)
