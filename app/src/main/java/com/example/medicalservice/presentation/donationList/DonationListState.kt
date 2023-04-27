package com.example.medicalservice.presentation.donationList

import com.example.models.app.DonationRequest

data class DonationListState(
    val donationRequests: List<DonationRequest> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
)
