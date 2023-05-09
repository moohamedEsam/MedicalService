package com.example.medicalservice.presentation.home.donner

import com.example.model.app.DonationRequestView

sealed interface DonnerHomeScreenEvent {
    data class OnQueryChange(val query: String) : DonnerHomeScreenEvent

    data class OnDonationRequestClick(val donationRequestId: String) : DonnerHomeScreenEvent
    data class OnDonationRequestBookmarkClick(val donationRequest: DonationRequestView) :
        DonnerHomeScreenEvent

    data class OnMedicineClick(val medicineId: String) : DonnerHomeScreenEvent

    data class OnTransactionClick(val transactionId: String) : DonnerHomeScreenEvent
}
