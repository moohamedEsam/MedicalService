package com.example.domain.usecase

import com.example.model.app.DiseaseView

fun interface GetDiseaseDetailsUseCase : suspend (String) -> com.example.model.app.DiseaseView