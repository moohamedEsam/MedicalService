package com.example.medicalservice.presentation.home.donner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetCurrentUserUseCase
import com.example.medicalservice.domain.GetMostNeededMedicineUseCase
import com.example.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DonnerHomeViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getMostNeededMedicineUseCase: GetMostNeededMedicineUseCase
) : ViewModel() {
    private val _user: MutableStateFlow<User> = MutableStateFlow(User.emptyDonor())
    val user = _user.asStateFlow()

    private val _mostNeededMedicine = MutableStateFlow(emptyList<DonationRequest>())
    val mostNeededMedicine = _mostNeededMedicine.asStateFlow()

    private val _showTransactionDialog = MutableStateFlow(false)
    val showTransactionDialog = _showTransactionDialog.asStateFlow()

    var transaction: Transaction? = null
        private set

    init {
        viewModelScope.launch {
            _user.value = getCurrentUserUseCase()
        }

        viewModelScope.launch {
            _mostNeededMedicine.value = getMostNeededMedicineUseCase()
        }
    }

    fun onTransactionClick(transaction: Transaction) {
        this.transaction = transaction
        _showTransactionDialog.value = true
    }

    fun onTransactionDialogDismiss() {
        _showTransactionDialog.value = false
    }
}