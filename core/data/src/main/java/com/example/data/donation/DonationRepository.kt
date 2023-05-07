package com.example.data.donation

import androidx.paging.PagingSource
import com.example.model.app.DonationRequestView
import kotlinx.coroutines.flow.Flow

sealed interface DonationRepository {
    fun getDonationRequests(): PagingSource<Int, DonationRequestView>

    fun getDonationRequest(id: String): Flow<DonationRequestView>
    suspend fun syncDonationRequests(): Boolean
}