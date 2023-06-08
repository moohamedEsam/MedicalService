package com.example.medicalservice.presentation.diagnosisRequest.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.navigation.AppNavigator
import com.example.domain.usecase.diagnosis.CreateDiagnosisRequestUseCase
import com.example.domain.usecase.disease.GetAvailableSymptomsUseCase
import com.example.functions.snackbar.SnackBarManager
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.disease.Symptom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DiagnosisFormViewModel(
    private val getAvailableSymptomsUseCase: GetAvailableSymptomsUseCase,
    private val createDiagnosisRequestUseCase: CreateDiagnosisRequestUseCase,
    private val snackBarManager: SnackBarManager,
    private val appNavigator: AppNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiagnosisFormState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(symptoms = getAvailableSymptomsUseCase())
        }
    }

    fun handleEvent(event: DiagnosisFormEvent) = viewModelScope.launch {
        when (event) {
            is DiagnosisFormEvent.OnDescriptionChange -> _uiState.value =
                _uiState.value.copy(description = event.description)

            is DiagnosisFormEvent.OnQueryChange -> _uiState.value =
                _uiState.value.copy(query = event.query)

            DiagnosisFormEvent.OnSubmitClick -> createDiagnosisRequest()
            is DiagnosisFormEvent.OnSymptomClick -> toggleSymptomSelection(event.symptom)
        }
    }

    private fun createDiagnosisRequest() = viewModelScope.launch {
        if (!_uiState.value.isDiagnosisButtonEnabled) return@launch
        _uiState.value = _uiState.value.copy(isLoading = true)
        val result = createDiagnosisRequestUseCase(
            DiagnosisRequest(
                symptoms = uiState.value.selectedSymptoms,
                description = uiState.value.description,
            )
        )
        _uiState.value = _uiState.value.copy(isLoading = false)
        val event = result.getSnackBarEvent(
            successMessage = "Diagnosis request created successfully",
            errorActionLabel = "Retry",
            errorAction = ::createDiagnosisRequest
        )
        snackBarManager.showSnackBarEvent(event)
        result.ifSuccess { appNavigator.navigateBack() }
    }

    private fun toggleSymptomSelection(symptom: Symptom) = viewModelScope.launch {
        _uiState.update {
            if (it.selectedSymptoms.contains(symptom))
                it.copy(selectedSymptoms = it.selectedSymptoms - symptom)
            else
                it.copy(selectedSymptoms = it.selectedSymptoms + symptom)
        }
    }
}