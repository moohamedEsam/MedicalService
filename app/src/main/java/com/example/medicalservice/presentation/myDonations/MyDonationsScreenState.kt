package com.example.medicalservice.presentation.myDonations

import androidx.paging.PagingData
import androidx.paging.filter
import com.example.model.app.donation.DonationRequestView
import com.example.model.app.transaction.TransactionView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

data class MyDonationsScreenState(
    val donationRequests: Flow<PagingData<DonationRequestView>> = flowOf(PagingData.empty()),
    val transactions: Flow<PagingData<TransactionView>> = flowOf(PagingData.empty()),
    val query: String = ""
) {
    val filteredDonationRequests: Flow<PagingData<DonationRequestView>> = donationRequests
        .map { pagingData ->
            pagingData.filter { donationRequestView ->
                donationRequestView.medicine.name.contains(query, ignoreCase = true)
                        || donationRequestView.medicine.diseases.any {
                    it.name.contains(
                        query,
                        ignoreCase = true
                    )
                }
            }
        }

    val filteredTransactions: Flow<PagingData<TransactionView>> = transactions
        .map { pagingData ->
            pagingData.filter { transactionView ->
                transactionView.medicine.name.contains(query, ignoreCase = true)
                        || transactionView.medicine.diseases.any {
                    it.name.contains(
                        query,
                        ignoreCase = true
                    )
                }
            }
        }
}
