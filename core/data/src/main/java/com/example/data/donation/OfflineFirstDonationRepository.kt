package com.example.data.donation

import androidx.paging.PagingSource
import com.example.common.models.Result
import com.example.database.models.donation.DonationRequestEntityView
import com.example.database.models.donation.toDonationRequestView
import com.example.database.models.donation.toEntity
import com.example.database.room.dao.DonationRequestDao
import com.example.model.app.DonationRequestView
import com.example.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single([DonationRepository::class])
class OfflineFirstDonationRepository(
    private val remote: RemoteDataSource,
    private val local: DonationRequestDao,
) : DonationRepository {
    override fun getDonationRequests(): PagingSource<Int, DonationRequestView> =
        local.getDonationRequests().map { it.toDonationRequestView() }
            .asPagingSourceFactory().invoke()

    override suspend fun syncDonationRequests(): Boolean {
        val donationRequests = remote.getDonationRequests()
        donationRequests.ifSuccess {
            local.deleteAll()
            local.insertAll(it.map { request -> request.toEntity() })
        }
        return donationRequests is Result.Success
    }

    override fun getDonationRequest(id: String): Flow<DonationRequestView> =
        local.getDonationRequestById(id).filterNotNull().map(DonationRequestEntityView::toDonationRequestView)
}