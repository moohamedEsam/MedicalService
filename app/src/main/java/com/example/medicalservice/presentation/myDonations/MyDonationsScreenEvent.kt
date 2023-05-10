package com.example.medicalservice.presentation.myDonations

import com.example.model.app.DonationRequestView
import com.example.model.app.TransactionView

sealed interface MyDonationsScreenEvent {
    data class QueryChanged(val query: String) : MyDonationsScreenEvent
    data class OnDonationRequestClick(val donationRequest: DonationRequestView) : MyDonationsScreenEvent
    data class OnDonationRequestBookmarkClick(val donationRequest: DonationRequestView) : MyDonationsScreenEvent
    data class OnTransactionClick(val transaction: TransactionView) : MyDonationsScreenEvent
    data class OnMedicineClick(val medicineId: String) : MyDonationsScreenEvent
}