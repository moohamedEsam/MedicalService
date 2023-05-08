package com.example.domain.usecase.medicine

import com.example.model.app.medicine.MedicineView

fun interface GetMedicinesUseCase : suspend () -> List<MedicineView>