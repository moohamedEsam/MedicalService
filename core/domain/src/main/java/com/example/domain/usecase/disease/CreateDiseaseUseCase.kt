package com.example.domain.usecase.disease

import com.example.common.models.Result
import com.example.model.app.disease.Disease

fun interface CreateDiseaseUseCase : suspend (Disease) -> Result<Disease>