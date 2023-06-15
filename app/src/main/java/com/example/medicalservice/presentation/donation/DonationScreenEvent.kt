package com.example.medicalservice.presentation.donation

sealed interface DonationScreenEvent {
    object OnDonateClick : DonationScreenEvent
    data class OnQuantityChange(val quantity: String) : DonationScreenEvent

    object OnMedicineReadMoreClick: DonationScreenEvent
}