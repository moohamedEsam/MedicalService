package com.example.data.donation

import androidx.paging.PagingSource
import com.example.model.app.donation.DonationRequestView
import kotlinx.coroutines.flow.Flow

sealed interface DonationRepository {
    fun getDonationRequests(): () -> PagingSource<Int, DonationRequestView>

    fun getBookmarkedDonationRequests(): () -> PagingSource<Int, DonationRequestView>
    fun getDonationRequest(id: String): Flow<DonationRequestView>

    suspend fun setDonationRequestBookmark(id: String, isBookmarked: Boolean)
    suspend fun syncDonationRequests(): Boolean
}