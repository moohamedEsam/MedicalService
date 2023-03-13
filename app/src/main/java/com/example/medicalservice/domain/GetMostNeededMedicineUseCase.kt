package com.example.medicalservice.domain

import com.example.models.DonationRequest

fun interface GetMostNeededMedicineUseCase : suspend () -> List<DonationRequest>