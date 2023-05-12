package com.example.medicalservice.presentation.home.donner

import androidx.paging.PagingData
import com.example.model.app.donation.DonationRequestView
import com.example.model.app.transaction.TransactionView
import com.example.model.app.user.User
import com.example.model.app.user.User.Donor
import com.example.model.app.user.emptyDonor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class DonnerHomeState(
    val user: Donor = User.emptyDonor(),
    val donationRequestViews: Flow<PagingData<DonationRequestView>> = flowOf(PagingData.empty()),
    val transactionViews: Flow<PagingData<TransactionView>> = flowOf(PagingData.empty()),
    val query: String = "",
)
