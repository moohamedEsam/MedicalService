package com.example.medicalservice.presentation.donationList

import com.example.model.app.DonationRequestView

sealed interface DonationListScreenState{
    data class OnDonationRequestBookmarkClick(val donationRequestView: DonationRequestView) : DonationListScreenState
    data class OnDonationRequestClick(val donationRequestView: DonationRequestView) : DonationListScreenState

}