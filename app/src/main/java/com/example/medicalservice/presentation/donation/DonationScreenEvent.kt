package com.example.medicalservice.presentation.donation

sealed interface DonationScreenEvent {
    data class OnDonationRequestSelected(val donationRequestId: String) : DonationScreenEvent
    data class OnQueryChange(val query: String) : DonationScreenEvent
    object OnChooseAnotherDonationRequest : DonationScreenEvent
    object OnDonateClick : DonationScreenEvent
    data class OnQuantityChange(val quantity: String) : DonationScreenEvent
}