package com.example.domain.usecase

fun interface GetDonationRequestsUseCase : suspend () -> List<com.example.model.app.DonationRequestView>