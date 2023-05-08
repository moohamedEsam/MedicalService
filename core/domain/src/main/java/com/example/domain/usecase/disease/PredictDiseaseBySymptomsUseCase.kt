package com.example.domain.usecase.disease

fun interface PredictDiseaseBySymptomsUseCase : suspend (String) -> List<com.example.model.app.DiseaseView>