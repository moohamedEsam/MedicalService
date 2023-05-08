package com.example.domain.usecase.medicine

import com.example.model.app.medicine.MedicineView
import kotlinx.coroutines.flow.Flow

fun interface GetMedicineDetailsUseCase : suspend (String) -> Flow<MedicineView>