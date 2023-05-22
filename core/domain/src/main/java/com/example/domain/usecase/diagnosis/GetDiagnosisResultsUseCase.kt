package com.example.domain.usecase.diagnosis

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.model.app.diagnosis.DiagnosisResultView

fun interface GetDiagnosisResultsUseCase : () -> () -> PagingSource<Int, DiagnosisResultView>