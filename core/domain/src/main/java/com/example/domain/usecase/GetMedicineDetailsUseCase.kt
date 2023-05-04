package com.example.domain.usecase

import com.example.model.app.MedicineView

fun interface GetMedicineDetailsUseCase : suspend (String) -> com.example.model.app.MedicineView