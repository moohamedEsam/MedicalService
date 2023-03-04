package com.example.medicalservice.presentation.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetMedicineDetailsUseCase
import com.example.models.MedicineView
import com.example.models.empty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MedicineDetailsViewModel(
    private val getMedicineDetailsUseCase: GetMedicineDetailsUseCase,
    private val medicineId: String
) : ViewModel() {
    private val _medicine = MutableStateFlow(MedicineView.empty())
    val medicine = _medicine.asStateFlow()

    init {
        viewModelScope.launch {
            _medicine.value = getMedicineDetailsUseCase(medicineId)
        }
    }

}