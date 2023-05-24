package com.example.medicalservice.presentation.diagnosisResult.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.diagnosis.GetDiagnosisResultByIdUseCase
import com.example.domain.usecase.disease.GetDiseasesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DiagnosisResultFormViewModel(
    private val getDiagnosisResultByIdUseCase: GetDiagnosisResultByIdUseCase,
    private val getDiseasesUseCase: GetDiseasesUseCase,
    private val appNavigator: AppNavigator,
    private val diagnosisResultId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiagnosisResultFormState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDiagnosisResultByIdUseCase(diagnosisResultId).collectLatest {
                _uiState.value = uiState.value.copy(
                    diagnosisRequest = it.request,
                    diagnosis = it.diagnosis
                )
            }
            getDiseasesUseCase().collectLatest {
                _uiState.value = uiState.value.copy(diseaseOptions = it)
            }
        }
    }

    fun handleEvent(event: DiagnosisResultFormEvent) = viewModelScope.launch {
        when (event) {
            DiagnosisResultFormEvent.OnAddDiseaseClick -> _uiState.value = uiState.value.copy(
                isDiseaseOptionDialogVisible = true,
                diseaseOptionDialogSearchQuery = ""
            )

            DiagnosisResultFormEvent.OnAddMedicineClick -> _uiState.value = uiState.value.copy(
                isMedicineOptionDialogVisible = true,
                medicineOptionDialogSearchQuery = ""
            )

            DiagnosisResultFormEvent.OnAddUnRegisteredDiseaseClick -> _uiState.value =
                uiState.value.copy(
                    isUnregisteredDiseaseDialogVisible = true,
                    unregisteredDiseaseValue = ""
                )

            DiagnosisResultFormEvent.OnAddUnRegisteredMedicineClick -> _uiState.value =
                uiState.value.copy(
                    isUnregisteredMedicineDialogVisible = true,
                    unregisteredMedicineValue = ""
                )

            is DiagnosisResultFormEvent.OnDiagnosisChange -> _uiState.value =
                uiState.value.copy(diagnosis = event.diagnosis)

            DiagnosisResultFormEvent.OnDiseaseClick -> appNavigator.navigateTo(Destination.DiseaseDetails(uiState.value.disease?.id ?: ""))

            is DiagnosisResultFormEvent.OnMedicineClick -> appNavigator.navigateTo(Destination.MedicineDetails(event.medicineId))

            is DiagnosisResultFormEvent.OnMedicineDelete -> _uiState.value =
                uiState.value.copy(medicationsIds = uiState.value.medicationsIds - event.medicineId)

            DiagnosisResultFormEvent.OnRemoveDiseaseClick -> _uiState.value =
                uiState.value.copy(disease = null)

            is DiagnosisResultFormEvent.OnUnRegisteredMedicineDelete -> _uiState.value =
                uiState.value.copy(unRegisteredMedicines = uiState.value.unRegisteredMedicines - event.medicineName)

            DiagnosisResultFormEvent.OnSaveClick -> Unit
        }
    }
}