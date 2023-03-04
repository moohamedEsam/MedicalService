package com.example.medicalservice.domain

import com.example.models.MedicineView

fun interface GetMedicineDetailsUseCase : suspend (String) -> MedicineView