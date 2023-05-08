package com.example.domain.usecase.disease

import kotlinx.coroutines.flow.Flow

fun interface GetDiseaseDetailsUseCase : suspend (String) -> Flow<com.example.model.app.DiseaseView>