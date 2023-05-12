package com.example.domain.usecase.disease

import com.example.model.app.disease.DiseaseView

fun interface PredictDiseaseBySymptomsUseCase : suspend (String) -> List<DiseaseView>