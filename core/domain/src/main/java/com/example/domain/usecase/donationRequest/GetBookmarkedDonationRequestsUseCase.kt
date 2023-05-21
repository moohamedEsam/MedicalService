package com.example.domain.usecase.donationRequest

import androidx.paging.PagingSource
import com.example.model.app.donation.DonationRequestView

fun interface GetBookmarkedDonationRequestsUseCase : () -> (() -> PagingSource<Int, DonationRequestView>)