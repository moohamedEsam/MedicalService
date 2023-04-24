package com.example.medicalservice.domain

import com.example.models.app.DiseaseView

fun interface PredictDiseaseBySymptomsUseCase : suspend (String) -> List<DiseaseView>