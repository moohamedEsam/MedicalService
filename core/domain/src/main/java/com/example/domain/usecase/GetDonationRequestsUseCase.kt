package com.example.domain.usecase

import com.example.model.app.DonationRequest

fun interface GetDonationRequestsUseCase : suspend () -> List<com.example.model.app.DonationRequest>