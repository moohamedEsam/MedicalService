package com.example.medicalservice.presentation.donationList

import androidx.paging.PagingData
import com.example.model.app.donation.DonationRequestView
import com.example.model.app.transaction.TransactionView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class DonationListState(
    val donationRequestViews: Flow<PagingData<DonationRequestView>> = emptyFlow(),
    val transactions: Flow<PagingData<TransactionView>> = emptyFlow(),
    val query: String = "",
    val isLoading: Boolean = false,
    val isConfirmationDialogVisible: Boolean = false,
    val selectedTransactionView: TransactionView? = null,
)
