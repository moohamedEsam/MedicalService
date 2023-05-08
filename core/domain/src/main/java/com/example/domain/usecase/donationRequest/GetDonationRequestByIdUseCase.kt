package com.example.domain.usecase.donationRequest

import com.example.model.app.DonationRequestView
import kotlinx.coroutines.flow.Flow

fun interface GetDonationRequestByIdUseCase : (String) -> Flow<DonationRequestView>