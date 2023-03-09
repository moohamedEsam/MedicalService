package com.example.medicalservice.presentation.home.donner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetCurrentUserUseCase
import com.example.medicalservice.domain.GetMostNeededMedicineUseCase
import com.example.models.Medicine
import com.example.models.User
import com.example.models.emptyDonor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DonnerHomeViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getMostNeededMedicineUseCase: GetMostNeededMedicineUseCase
) : ViewModel() {
    private val _user: MutableStateFlow<User> = MutableStateFlow(User.emptyDonor())
    val user = _user.asStateFlow()

    private val _mostNeededMedicine = MutableStateFlow(emptyList<Medicine>())
    val mostNeededMedicine = _mostNeededMedicine.asStateFlow()

    init{
        viewModelScope.launch {
            _user.value = getCurrentUserUseCase()
        }

        viewModelScope.launch {
            _mostNeededMedicine.value = getMostNeededMedicineUseCase()
        }
    }
}