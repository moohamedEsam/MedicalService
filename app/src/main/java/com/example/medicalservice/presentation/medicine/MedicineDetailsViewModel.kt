package com.example.medicalservice.presentation.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetMedicineDetailsUseCase
import com.example.models.app.MedicineView
import com.example.models.app.empty
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MedicineDetailsViewModel(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val medicineId: String,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    private val _medicine = MutableStateFlow(MedicineView.empty())
    val medicine = _medicine.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            _medicine.value = getMedicineDetailsUseCase(medicineId)
        }
    }

}