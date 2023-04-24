package com.example.medicalservice.presentation.home.donner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetCurrentUserUseCase
import com.example.medicalservice.domain.GetDonationRequestsUseCase
import com.example.models.*
import com.example.models.app.DonationRequest
import com.example.models.app.Transaction
import com.example.models.app.User
import com.example.models.app.emptyDonor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonnerHomeViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    private val _user: MutableStateFlow<User> = MutableStateFlow(User.emptyDonor())
    val user = _user.asStateFlow()

    private val _mostNeededMedicine = MutableStateFlow(emptyList<DonationRequest>())
    val mostNeededMedicine = _mostNeededMedicine.asStateFlow()

    private val _showTransactionDialog = MutableStateFlow(false)
    val showTransactionDialog = _showTransactionDialog.asStateFlow()

    private val _transaction: MutableStateFlow<Transaction?> = MutableStateFlow(null)
    val transaction = _transaction.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            _user.value = getCurrentUserUseCase()
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            _mostNeededMedicine.value = getDonationRequestsUseCase()
        }
    }

    fun onTransactionClick(transaction: Transaction) {
        _transaction.value = transaction
        _showTransactionDialog.value = true
    }

    fun onTransactionDialogDismiss() {
        _showTransactionDialog.value = false
    }
}