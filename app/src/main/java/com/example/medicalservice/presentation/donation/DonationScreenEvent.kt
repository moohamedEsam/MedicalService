package com.example.medicalservice.presentation.donation

import com.example.model.app.DonationRequestView

sealed interface DonationScreenEvent {
    data class OnDonationRequestSelected(val donationRequest: DonationRequestView) : DonationScreenEvent
    data class OnQueryChange(val query: String) : DonationScreenEvent
    object OnChooseAnotherDonationRequest : DonationScreenEvent
    object OnDonateClick : DonationScreenEvent
    data class OnQuantityChange(val quantity: String) : DonationScreenEvent
}