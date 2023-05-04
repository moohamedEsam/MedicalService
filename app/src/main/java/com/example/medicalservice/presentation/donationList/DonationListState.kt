package com.example.medicalservice.presentation.donationList

data class DonationListState(
    val donationRequestViews: List<com.example.model.app.DonationRequestView> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
)
