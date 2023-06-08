package com.example.medicalservice.presentation.diagnosisResult.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.models.Result
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.diagnosis.GetDiagnosisResultByIdUseCase
import com.example.domain.usecase.diagnosis.UpdateDiagnosisResultUseCase
import com.example.domain.usecase.disease.CreateDiseaseUseCase
import com.example.domain.usecase.disease.GetDiseasesUseCase
import com.example.domain.usecase.medicine.CreateMedicineUseCase
import com.example.domain.usecase.user.GetCurrentUserIdUseCase
import com.example.functions.snackbar.SnackBarManager
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.empty
import com.example.model.app.disease.toDisease
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.Date

@KoinViewModel
class DiagnosisResultFormViewModel(
    private val getDiagnosisResultByIdUseCase: GetDiagnosisResultByIdUseCase,
    private val createMedicineUseCase: CreateMedicineUseCase,
    private val createDiseaseUseCase: CreateDiseaseUseCase,
    private val getDiseasesUseCase: GetDiseasesUseCase,
    private val updateDiagnosisResultUseCase: UpdateDiagnosisResultUseCase,
    private val appNavigator: AppNavigator,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val snackBarManager: SnackBarManager,
    private val diagnosisResultId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiagnosisResultFormState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDiagnosisResultByIdUseCase(diagnosisResultId).collectLatest {
                _uiState.value = uiState.value.copy(
                    diagnosisRequest = it.request,
                    diagnosis = it.diagnosis,
                    disease = it.disease,
                    medicationsIds = it.medications.map { medicine -> medicine.id },
                )
            }
        }
        viewModelScope.launch {
            getDiseasesUseCase().collectLatest {
                _uiState.value = uiState.value.copy(diseaseOptions = it)
            }
        }
    }

    fun handleEvent(event: DiagnosisResultFormEvent) = viewModelScope.launch {
        when (event) {
            is DiagnosisResultFormEvent.Form -> handleFormEvent(event)
            is DiagnosisResultFormEvent.UnregisteredMedicineDialog -> handleUnregisteredMedicineDialogEvent(
                event
            )

            is DiagnosisResultFormEvent.UnregisteredDiseaseDialog -> handleUnregisteredDiseaseDialogEvent(
                event
            )

            is DiagnosisResultFormEvent.MedicineOptionSearch -> handleMedicineOptionSearchEvent(
                event
            )

            is DiagnosisResultFormEvent.DiseaseOptionSearch -> handleDiseaseOptionSearchEvent(event)
        }
    }

    private suspend fun handleFormEvent(event: DiagnosisResultFormEvent.Form) = when (event) {
        DiagnosisResultFormEvent.Form.OnAddDiseaseClick -> _uiState.value = uiState.value.copy(
            isDiseaseSearchBarVisible = true,
            diseaseOptionsSearchQuery = ""
        )

        DiagnosisResultFormEvent.Form.OnAddMedicineClick -> _uiState.value = uiState.value.copy(
            isMedicineOptionSearchVisible = !_uiState.value.isMedicineOptionSearchVisible,
            medicineOptionSearchQuery = ""
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

        DiagnosisResultFormEvent.Form.OnSaveClick -> {
            createUnregisteredDiseaseAndMedicines()
            save()
        }
    }

    private fun handleDiseaseOptionSearchEvent(event: DiagnosisResultFormEvent.DiseaseOptionSearch) =
        when (event) {
            is DiagnosisResultFormEvent.DiseaseOptionSearch.OnDiseaseClick -> _uiState.value =
                uiState.value.copy(
                    disease = _uiState.value.diseaseOptions.find { it.id == event.diseaseId },
                    isDiseaseSearchBarVisible = false
                )

            DiagnosisResultFormEvent.DiseaseOptionSearch.Dismiss -> _uiState.value =
                uiState.value.copy(isDiseaseSearchBarVisible = false)

            is DiagnosisResultFormEvent.DiseaseOptionSearch.OnQueryChange -> _uiState.value =
                uiState.value.copy(diseaseOptionsSearchQuery = event.query)
        }

    private fun handleMedicineOptionSearchEvent(event: DiagnosisResultFormEvent.MedicineOptionSearch) =
        when (event) {
            is DiagnosisResultFormEvent.MedicineOptionSearch.OnMedicineClick -> _uiState.value =
                uiState.value.copy(medicationsIds = uiState.value.medicationsIds + event.medicineId)

            DiagnosisResultFormEvent.MedicineOptionSearch.Dismiss -> _uiState.value =
                uiState.value.copy(isMedicineOptionSearchVisible = false)

            is DiagnosisResultFormEvent.MedicineOptionSearch.OnQueryChange -> _uiState.value =
                uiState.value.copy(medicineOptionSearchQuery = event.query)
        }

    private fun handleUnregisteredDiseaseDialogEvent(event: DiagnosisResultFormEvent.UnregisteredDiseaseDialog) =
        when (event) {
            is DiagnosisResultFormEvent.UnregisteredDiseaseDialog.OnSaveClick -> {
                if (uiState.value.unregisteredDiseaseValue.isNotBlank())
                    _uiState.value =
                        uiState.value.copy(
                            disease = DiseaseView.empty()
                                .copy(name = uiState.value.unregisteredDiseaseValue),
                            isUnregisteredDiseaseDialogVisible = false
                        )
                Unit
            }

            DiagnosisResultFormEvent.UnregisteredDiseaseDialog.Dismiss -> _uiState.value =
                uiState.value.copy(isUnregisteredDiseaseDialogVisible = false)

            is DiagnosisResultFormEvent.UnregisteredDiseaseDialog.OnDiseaseChange -> _uiState.value =
                uiState.value.copy(unregisteredDiseaseValue = event.disease)
        }

    private fun handleUnregisteredMedicineDialogEvent(event: DiagnosisResultFormEvent.UnregisteredMedicineDialog) =
        when (event) {
            is DiagnosisResultFormEvent.UnregisteredMedicineDialog.OnSaveClick -> {
                if (uiState.value.unregisteredMedicineValue.isNotBlank())
                    _uiState.value =
                        uiState.value.copy(
                            unRegisteredMedicines = uiState.value.unRegisteredMedicines + uiState.value.unregisteredMedicineValue,
                            isUnregisteredMedicineDialogVisible = false
                        )
                Unit
            }

            DiagnosisResultFormEvent.UnregisteredMedicineDialog.Dismiss -> _uiState.value =
                uiState.value.copy(isUnregisteredMedicineDialogVisible = false)

            is DiagnosisResultFormEvent.UnregisteredMedicineDialog.OnMedicineChange -> _uiState.value =
                uiState.value.copy(unregisteredMedicineValue = event.medicine)
        }

    private suspend fun save() {
        val result = updateDiagnosisResultUseCase(getDiagnosisResultFromState())
        val event = result.getSnackBarEvent(
            successMessage = "Diagnosis saved successfully",
            errorActionLabel = "Retry",
            errorAction = ::save,
        )
        snackBarManager.showSnackBarEvent(event)
        result.ifSuccess {
            appNavigator.navigateBack()
        }
    }

    private suspend fun createUnregisteredDiseaseAndMedicines() {
        _uiState.update { state ->
            if (state.isDiseaseInDatabase || state.disease == null) return@update state
            val result = createDiseaseUseCase(state.disease.toDisease())
            if (result is Result.Success)
                return@update state.copy(isDiseaseInDatabase = true)
            state
        }

        _uiState.update { state ->
            val ids = mutableMapOf<String, String>()
            for (medicineName in state.unRegisteredMedicines) {
                val result = createMedicineUseCase(
                    Medicine.empty()
                        .copy(name = medicineName, diseasesId = listOf(state.disease?.id ?: ""))
                )
                if (result is Result.Success)
                    ids += medicineName to result.data.id
            }

            state.copy(
                unRegisteredMedicines = state.unRegisteredMedicines - ids.keys,
                medicationsIds = state.medicationsIds + ids.values
            )
        }
    }

    private suspend fun getDiagnosisResultFromState() = uiState.value.let {
        DiagnosisResult(
            id = diagnosisResultId,
            diagnosis = it.diagnosis,
            doctorId = getCurrentUserIdUseCase(),
            status = DiagnosisResult.Status.Complete,
            createdAt = Date(),
            updatedAt = Date(),
            diagnosisRequestId = it.diagnosisRequest.id,
            diseaseId = it.disease?.id ?: "",
            medicationsIds = it.medicationsIds,
        )
    }
}