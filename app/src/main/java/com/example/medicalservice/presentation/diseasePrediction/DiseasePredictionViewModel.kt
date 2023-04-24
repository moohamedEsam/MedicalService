package com.example.medicalservice.presentation.diseasePrediction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetAvailableSymptomsUseCase
import com.example.medicalservice.domain.PredictDiseaseBySymptomsUseCase
import com.example.models.app.DiseaseView
import com.example.models.app.Symptom
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DiseasePredictionViewModel(
    private val getAvailableSymptomsUseCase: GetAvailableSymptomsUseCase,
    private val predictDiseaseBySymptomsUseCase: PredictDiseaseBySymptomsUseCase,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    private val _symptoms = MutableStateFlow(emptyList<Symptom>())
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val symptoms = combine(_symptoms, _query) { symptoms, query ->
        symptoms.filter { it.name.contains(query, true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _selectedSymptoms = MutableStateFlow(emptySet<Symptom>())
    val selectedSymptoms = _selectedSymptoms.asStateFlow()

    private val _diseases = MutableStateFlow(emptyList<DiseaseView>())
    val diseases = _diseases.asStateFlow()

    val isPredictButtonEnabled = _selectedSymptoms.map { selectedSymptoms ->
        selectedSymptoms.size > 4
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) { _symptoms.value = getAvailableSymptomsUseCase() }
    }

    fun onSymptomSelected(symptom: Symptom) {
        _selectedSymptoms.update {
            if (it.contains(symptom)) {
                it - symptom
            } else {
                it + symptom
            }
        }
    }

    fun onPredictClick() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _isLoading.value = true
            val symptomsEncoded = List(selectedSymptoms.value.size) { index ->
                if (_symptoms.value[index] in selectedSymptoms.value) '1' else '0'
            }.joinToString("")
            _diseases.value = predictDiseaseBySymptomsUseCase(symptomsEncoded)
            _isLoading.value = false
        }
    }

    fun onQueryChanged(query: String) {
        _query.value = query
    }

}