package com.example.medicalservice.domain

import com.example.models.app.Symptom

fun interface GetAvailableSymptomsUseCase : suspend () -> List<Symptom>