package com.example.medicalservice.domain

import com.example.models.app.MedicineView

fun interface GetMedicineDetailsUseCase : suspend (String) -> MedicineView