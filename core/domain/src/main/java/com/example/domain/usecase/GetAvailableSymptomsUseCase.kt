package com.example.domain.usecase

import com.example.model.app.Symptom


fun interface GetAvailableSymptomsUseCase : suspend () -> List<Symptom>