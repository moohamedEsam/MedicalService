package com.example.medicalservice.domain

import com.example.models.DonationRequest

fun interface GetDonationRequestsUseCase : suspend () -> List<DonationRequest>