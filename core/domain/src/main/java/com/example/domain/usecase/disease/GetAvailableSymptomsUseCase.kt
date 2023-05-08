package com.example.domain.usecase.disease

import com.example.model.app.Symptom


fun interface GetAvailableSymptomsUseCase : suspend () -> List<Symptom>