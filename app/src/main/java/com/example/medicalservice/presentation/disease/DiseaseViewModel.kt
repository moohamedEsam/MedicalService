package com.example.medicalservice.presentation.disease

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.disease.GetDiseaseDetailsUseCase
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.empty
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DiseaseViewModel(
    private val getDiseaseDetailsUseCase: GetDiseaseDetailsUseCase,
    private val appNavigator: AppNavigator,
    private val diseaseId: String,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    private val _disease = MutableStateFlow(DiseaseView.empty())
    val disease = _disease.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            getDiseaseDetailsUseCase(diseaseId).collect(_disease::emit)
        }
    }

    fun onMedicineClick(medicineId: String) = viewModelScope.launch {
        appNavigator.navigateTo(Destination.MedicineDetails(medicineId))
    }
}