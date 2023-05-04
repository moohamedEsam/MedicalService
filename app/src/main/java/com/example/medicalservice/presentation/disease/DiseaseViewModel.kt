package com.example.medicalservice.presentation.disease

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetDiseaseDetailsUseCase
import com.example.model.app.empty
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DiseaseViewModel(
    private val getDiseaseDetailsUseCase: GetDiseaseDetailsUseCase,
    private val diseaseId: String,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    private val _disease = MutableStateFlow(com.example.model.app.DiseaseView.empty())
    val disease = _disease.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            _disease.value = getDiseaseDetailsUseCase(diseaseId)
        }
    }
}