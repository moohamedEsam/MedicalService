package com.example.medicalservice.domain

import com.example.models.Medicine

fun interface GetMostNeededMedicineUseCase : suspend () -> List<Medicine>