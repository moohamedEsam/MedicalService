package com.example.medicalservice.presentation.diagnosisResult.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.diagnosis.CreateDiagnosisRequestUseCase
import com.example.domain.usecase.diagnosis.CreateDiagnosisResultUseCase
import com.example.domain.usecase.diagnosis.GetDiagnosisResultByIdUseCase
import com.example.domain.usecase.disease.CreateDiseaseUseCase
import com.example.domain.usecase.disease.GetDiseasesUseCase
import com.example.domain.usecase.medicine.CreateMedicineUseCase
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.disease.Disease
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.empty
import com.example.model.app.disease.toDisease
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.Date

@KoinViewModel
class DiagnosisResultFormViewModel(
    private val getDiagnosisResultByIdUseCase: GetDiagnosisResultByIdUseCase,
    private val createMedicineUseCase: CreateMedicineUseCase,
    private val createDiseaseUseCase: CreateDiseaseUseCase,
    private val getDiseasesUseCase: GetDiseasesUseCase,
    private val createDiagnosisResultUseCase: CreateDiagnosisResultUseCase,
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
            is DiagnosisResultFormEvent.Form -> handleFormEvent(event)
            is DiagnosisResultFormEvent.UnregisteredMedicineDialog -> handleUnregisteredMedicineDialogEvent(event)
            is DiagnosisResultFormEvent.UnregisteredDiseaseDialog -> handleUnregisteredDiseaseDialogEvent(event)
            is DiagnosisResultFormEvent.MedicineOptionDialog -> handleMedicineOptionDialogEvent(event)
            is DiagnosisResultFormEvent.DiseaseOptionDialog -> handleDiseaseOptionDialogEvent(event)
        }
    }

    private suspend fun handleFormEvent(event: DiagnosisResultFormEvent.Form) = when (event) {
        DiagnosisResultFormEvent.Form.OnAddDiseaseClick -> _uiState.value = uiState.value.copy(
            isDiseaseOptionDialogVisible = true,
            diseaseOptionDialogSearchQuery = ""
        )

        DiagnosisResultFormEvent.Form.OnAddMedicineClick -> _uiState.value = uiState.value.copy(
            isMedicineOptionDialogVisible = true,
            medicineOptionDialogSearchQuery = ""
        )

        DiagnosisResultFormEvent.Form.OnAddUnRegisteredDiseaseClick -> _uiState.value =
            uiState.value.copy(
                isUnregisteredDiseaseDialogVisible = true,
                unregisteredDiseaseValue = ""
            )

        DiagnosisResultFormEvent.Form.OnAddUnRegisteredMedicineClick -> _uiState.value =
            uiState.value.copy(
                isUnregisteredMedicineDialogVisible = true,
                unregisteredMedicineValue = ""
            )

        is DiagnosisResultFormEvent.Form.OnDiagnosisChange -> _uiState.value =
            uiState.value.copy(diagnosis = event.diagnosis)

        DiagnosisResultFormEvent.Form.OnDiseaseClick -> appNavigator.navigateTo(
            Destination.DiseaseDetails(
                uiState.value.disease?.id ?: ""
            )
        )

        is DiagnosisResultFormEvent.Form.OnMedicineClick -> appNavigator.navigateTo(
            Destination.MedicineDetails(
                event.medicineId
            )
        )

        is DiagnosisResultFormEvent.Form.OnMedicineDelete -> _uiState.value =
            uiState.value.copy(medicationsIds = uiState.value.medicationsIds - event.medicineId)

        DiagnosisResultFormEvent.Form.OnRemoveDiseaseClick -> _uiState.value =
            uiState.value.copy(
                disease = null,
                medicationsIds = emptyList(),
                unRegisteredMedicines = emptyList()
            )

        is DiagnosisResultFormEvent.Form.OnUnRegisteredMedicineDelete -> _uiState.value =
            uiState.value.copy(unRegisteredMedicines = uiState.value.unRegisteredMedicines - event.medicineName)

        DiagnosisResultFormEvent.Form.OnSaveClick -> save()
    }

    private fun handleDiseaseOptionDialogEvent(event: DiagnosisResultFormEvent.DiseaseOptionDialog) =
        when (event) {
            is DiagnosisResultFormEvent.DiseaseOptionDialog.OnDiseaseClick -> _uiState.value =
                uiState.value.copy(
                    disease = _uiState.value.diseaseOptions.find { it.id == event.diseaseId },
                    isDiseaseOptionDialogVisible = false
                )

            DiagnosisResultFormEvent.DiseaseOptionDialog.Dismiss -> _uiState.value =
                uiState.value.copy(isDiseaseOptionDialogVisible = false)

            is DiagnosisResultFormEvent.DiseaseOptionDialog.OnQueryChange -> _uiState.value =
                uiState.value.copy(diseaseOptionDialogSearchQuery = event.query)
        }
    private fun handleMedicineOptionDialogEvent(event: DiagnosisResultFormEvent.MedicineOptionDialog) =
        when (event) {
            is DiagnosisResultFormEvent.MedicineOptionDialog.OnMedicineClick -> _uiState.value =
                uiState.value.copy(
                    medicationsIds = uiState.value.medicationsIds + event.medicineId,
                    isMedicineOptionDialogVisible = false
                )

            DiagnosisResultFormEvent.MedicineOptionDialog.Dismiss -> _uiState.value =
                uiState.value.copy(isMedicineOptionDialogVisible = false)

            is DiagnosisResultFormEvent.MedicineOptionDialog.OnQueryChange -> _uiState.value =
                uiState.value.copy(medicineOptionDialogSearchQuery = event.query)
        }

    private fun handleUnregisteredDiseaseDialogEvent(event: DiagnosisResultFormEvent.UnregisteredDiseaseDialog) =
        when (event) {
            is DiagnosisResultFormEvent.UnregisteredDiseaseDialog.OnSaveClick -> _uiState.value =
                uiState.value.copy(
                    disease = DiseaseView.empty().copy(name = uiState.value.unregisteredDiseaseValue),
                    isUnregisteredDiseaseDialogVisible = false
                )

            DiagnosisResultFormEvent.UnregisteredDiseaseDialog.Dismiss -> _uiState.value =
                uiState.value.copy(isUnregisteredDiseaseDialogVisible = false)

            is DiagnosisResultFormEvent.UnregisteredDiseaseDialog.OnDiseaseChange -> _uiState.value =
                uiState.value.copy(unregisteredDiseaseValue = event.disease)
        }

    private fun handleUnregisteredMedicineDialogEvent(event: DiagnosisResultFormEvent.UnregisteredMedicineDialog) =
        when (event) {
            is DiagnosisResultFormEvent.UnregisteredMedicineDialog.OnSaveClick -> _uiState.value =
                uiState.value.copy(
                    unRegisteredMedicines = uiState.value.unRegisteredMedicines + uiState.value.unregisteredMedicineValue,
                    isUnregisteredMedicineDialogVisible = false
                )

            DiagnosisResultFormEvent.UnregisteredMedicineDialog.Dismiss -> _uiState.value =
                uiState.value.copy(isUnregisteredMedicineDialogVisible = false)

            is DiagnosisResultFormEvent.UnregisteredMedicineDialog.OnMedicineChange -> _uiState.value =
                uiState.value.copy(unregisteredMedicineValue = event.medicine)
        }

    private suspend fun save() {
        _uiState.value.let {
            if (!it.isDiseaseInDatabase && it.disease != null)
                createDiseaseUseCase(it.disease.toDisease())
            it.unRegisteredMedicines.forEach { medicineName ->
                createMedicineUseCase(
                    Medicine.empty().copy(name = medicineName, diseasesId = listOf(it.disease!!.id))
                )
            }
        }
        createDiagnosisResultUseCase(getDiagnosisResultFromState())
    }

    private fun getDiagnosisResultFromState() = uiState.value.let {
        DiagnosisResult(
            id = diagnosisResultId,
            diagnosis = it.diagnosis,
            doctorId = "",
            status = DiagnosisResult.Status.Pending,
            createdAt = Date(),
            updatedAt = Date(),
            diagnosisRequestId = it.diagnosisRequest.id
        )
    }
}