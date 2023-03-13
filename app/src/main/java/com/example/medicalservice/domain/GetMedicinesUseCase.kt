package com.example.medicalservice.domain

import com.example.models.MedicineView

fun interface GetMedicinesUseCase : suspend () -> List<MedicineView>