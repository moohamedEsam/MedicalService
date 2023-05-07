package com.example.medicalservice.presentation.home.donner

import androidx.paging.PagingData
import com.example.model.app.DonationRequestView
import com.example.model.app.TransactionView
import com.example.model.app.User.Donor
import com.example.model.app.emptyDonor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class DonnerHomeState(
    val user: Donor = com.example.model.app.User.emptyDonor(),
    val donationRequestViews: Flow<PagingData<DonationRequestView>> = flowOf(PagingData.empty()),
    val transactionViews: Flow<PagingData<TransactionView>> = flowOf(PagingData.empty()),
    val query: String = "",
)
