package com.example.domain.usecase.donationRequest

fun interface SetDonationRequestBookmarkUseCase : suspend (String, Boolean) -> Unit