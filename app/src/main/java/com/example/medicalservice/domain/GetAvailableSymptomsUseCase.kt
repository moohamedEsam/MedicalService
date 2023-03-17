package com.example.medicalservice.domain

import com.example.models.Symptom

fun interface GetAvailableSymptomsUseCase : suspend () -> List<Symptom>