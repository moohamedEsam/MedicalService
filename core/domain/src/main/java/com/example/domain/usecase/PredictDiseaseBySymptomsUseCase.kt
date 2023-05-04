package com.example.domain.usecase

import com.example.model.app.DiseaseView

fun interface PredictDiseaseBySymptomsUseCase : suspend (String) -> List<com.example.model.app.DiseaseView>