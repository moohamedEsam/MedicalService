package com.example.domain.usecase.disease

import com.example.model.app.disease.Symptom


fun interface GetAvailableSymptomsUseCase : suspend () -> List<Symptom>