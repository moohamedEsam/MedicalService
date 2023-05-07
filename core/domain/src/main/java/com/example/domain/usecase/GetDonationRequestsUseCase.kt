package com.example.domain.usecase

import androidx.paging.PagingSource
import com.example.model.app.DonationRequestView

fun interface GetDonationRequestsUseCase : () -> PagingSource<Int, DonationRequestView>