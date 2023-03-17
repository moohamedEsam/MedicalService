package com.example.medicalservice.presentation.donation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.models.ValidationResult
import com.example.common.validators.validateNumber
import com.example.medicalservice.domain.GetMedicinesUseCase
import com.example.models.MedicineView
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonationViewModel(
    private val getMedicinesUseCase: GetMedicinesUseCase
) : ViewModel() {

    private val _medicineQuery = MutableStateFlow("")
    val medicineQuery = _medicineQuery.asStateFlow()

    private val _medicines = MutableStateFlow(emptyList<MedicineView>())
    val medicines = combine(_medicines, medicineQuery) { medicines, query ->
        medicines.filter { it.name.contains(query, true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _selectedMedicineIndex = MutableStateFlow(-1)
    val selectedMedicine = _selectedMedicineIndex.map {
        if (it == -1) null else medicines.value[it]
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _quantity = MutableStateFlow("")
    val quantity = _quantity.asStateFlow()
    val quantityValidationResult = _quantity.map {
        val validationResult = validateNumber(it)
        if (validationResult is ValidationResult.Valid) {
            if (it.toInt() <= 0) ValidationResult.Invalid("Quantity must be greater than 0")
            else validationResult
        } else validationResult
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ValidationResult.Empty)

    val isDonateEnabled = combine(
        quantityValidationResult,
        selectedMedicine
    ) { quantityValidationResult, selectedMedicine ->
        quantityValidationResult is ValidationResult.Valid && selectedMedicine != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _medicines.value = getMedicinesUseCase()
        }
    }

    fun onMedicineQueryChanged(query: String) {
        _medicineQuery.value = query
    }

    fun onMedicineSelected(medicineView: MedicineView) {
        _selectedMedicineIndex.value = medicines.value.indexOf(medicineView)
    }

    fun onQuantityChanged(quantity: String) {
        _quantity.value = quantity
    }

    fun onDonateClick() {

    }
}