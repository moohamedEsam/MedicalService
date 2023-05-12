package com.example.medicalservice.presentation.home.donner

import com.example.model.app.donation.DonationRequestView
import com.example.model.app.transaction.TransactionView

sealed interface DonnerHomeScreenEvent {
    data class OnQueryChange(val query: String) : DonnerHomeScreenEvent

    data class OnDonationRequestClick(val donationRequestId: String) : DonnerHomeScreenEvent
    data class OnDonationRequestBookmarkClick(val donationRequest: DonationRequestView) : DonnerHomeScreenEvent

    object OnSeeAllDonationRequestsClick : DonnerHomeScreenEvent

    data class OnMedicineClick(val medicineId: String) : DonnerHomeScreenEvent

    data class OnTransactionClick(val transactionView: TransactionView) : DonnerHomeScreenEvent
}
