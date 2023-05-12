package com.example.medicalservice.presentation.donationList

import androidx.paging.PagingData
import com.example.model.app.donation.DonationRequestView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class DonationListState(
    val donationRequestViews: Flow<PagingData<DonationRequestView>> = flowOf(PagingData.empty()),
    val query: String = "",
    val isLoading: Boolean = false,
)
