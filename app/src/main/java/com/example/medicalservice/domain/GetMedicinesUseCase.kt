package com.example.medicalservice.domain

import com.example.models.app.MedicineView

fun interface GetMedicinesUseCase : suspend () -> List<MedicineView>