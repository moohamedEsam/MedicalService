package com.example.medicalservice.domain

import com.example.models.DiseaseView

fun interface PredictDiseaseBySymptomsUseCase : suspend (String) -> List<DiseaseView>