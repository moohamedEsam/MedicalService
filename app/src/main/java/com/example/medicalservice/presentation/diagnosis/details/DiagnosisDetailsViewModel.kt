package com.example.medicalservice.presentation.diagnosis.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.diagnosis.GetDiagnosisResultByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DiagnosisDetailsViewModel(
    private val appNavigator: AppNavigator,
    private val getDiagnosisResultByIdUseCase: GetDiagnosisResultByIdUseCase,
    private val diagnosisId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiagnosisDetailsScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDiagnosisResultByIdUseCase(diagnosisId).collectLatest {
                _uiState.value = _uiState.value.copy(diagnosisResultView = it)
            }
        }
    }

    fun handleEvent(event: DiagnosisDetailsScreenEvent) = viewModelScope.launch {
        when(event){
            DiagnosisDetailsScreenEvent.OnDoctorClick -> Unit
            is DiagnosisDetailsScreenEvent.OnMedicineClick -> appNavigator.navigateTo(Destination.MedicineDetails(event.medicineId))
        }
    }
}