package com.example.medicalservice.domain

import com.example.models.app.DiseaseView

fun interface GetDiseaseDetailsUseCase : suspend (String) -> DiseaseView