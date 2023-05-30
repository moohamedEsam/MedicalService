package com.example.medicalservice.presentation.donationList

import com.example.model.app.donation.DonationRequestView
import com.example.model.app.transaction.TransactionView

sealed interface DonationListScreenEvent{
    data class OnDonationRequestBookmarkClick(val donationRequestView: DonationRequestView) : DonationListScreenEvent
    data class OnDonationRequestClick(val donationRequestView: DonationRequestView) : DonationListScreenEvent

    data class OnTransactionClick(val transactionView: TransactionView) : DonationListScreenEvent

    data class OnMedicineClick(val medicineId: String) : DonationListScreenEvent

    sealed interface ConfirmationDialogEvent : DonationListScreenEvent {
        object OnConfirmClick : ConfirmationDialogEvent
        object OnCancelClick : ConfirmationDialogEvent
    }
}