package com.example.domain.usecase.disease

import com.example.model.app.disease.DiseaseView
import kotlinx.coroutines.flow.Flow

fun interface GetDiseasesUseCase : () -> Flow<List<DiseaseView>>