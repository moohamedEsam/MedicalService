package com.example.medicalservice.presentation.donationList

import com.example.model.app.donation.DonationRequestView

sealed interface DonationListScreenEvent{
    data class OnDonationRequestBookmarkClick(val donationRequestView: DonationRequestView) : DonationListScreenEvent
    data class OnDonationRequestClick(val donationRequestView: DonationRequestView) : DonationListScreenEvent

}