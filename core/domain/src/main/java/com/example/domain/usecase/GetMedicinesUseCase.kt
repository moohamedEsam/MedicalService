package com.example.domain.usecase

import com.example.model.app.MedicineView

fun interface GetMedicinesUseCase : suspend () -> List<com.example.model.app.MedicineView>