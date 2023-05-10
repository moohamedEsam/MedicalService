package com.example.medicalservice.presentation.myDonations

import androidx.paging.PagingData
import com.example.model.app.DonationRequestView
import com.example.model.app.TransactionView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MyDonationsScreenState(
    val donationRequests: Flow<PagingData<DonationRequestView>> = flowOf(PagingData.empty()),
    val transactions: Flow<PagingData<TransactionView>> = flowOf(PagingData.empty()),
    val query: String = ""
)
