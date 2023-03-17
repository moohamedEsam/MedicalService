package com.example.medicalservice.presentation.disease

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetDiseaseDetailsUseCase
import com.example.models.DiseaseView
import com.example.models.empty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DiseaseViewModel(
    private val getDiseaseDetailsUseCase: GetDiseaseDetailsUseCase,
    private val diseaseId: String
) : ViewModel() {
    private val _disease = MutableStateFlow(DiseaseView.empty())
    val disease = _disease.asStateFlow()

    init {
        viewModelScope.launch {
            _disease.value = getDiseaseDetailsUseCase(diseaseId)
        }
    }
}