package com.example.medicalservice.domain

import com.example.models.DiseaseView

fun interface GetDiseaseDetailsUseCase : suspend (String) -> DiseaseView