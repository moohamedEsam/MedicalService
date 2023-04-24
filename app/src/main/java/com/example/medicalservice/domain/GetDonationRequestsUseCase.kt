package com.example.medicalservice.domain

import com.example.models.app.DonationRequest

fun interface GetDonationRequestsUseCase : suspend () -> List<DonationRequest>