package com.example.domain.usecase.medicine

import com.example.common.models.Result
import com.example.model.app.medicine.Medicine

fun interface CreateMedicineUseCase : suspend (Medicine) -> Result<Medicine>