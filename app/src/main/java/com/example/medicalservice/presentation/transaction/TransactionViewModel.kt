package com.example.medicalservice.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TransactionViewModel(
    private val appNavigator: AppNavigator,
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase,
    private val transactionId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getTransactionDetailsUseCase(transactionId).distinctUntilChanged().collectLatest {
                _uiState.value = _uiState.value.copy(transactionView = it)
            }
        }
    }

    fun handleEvent(event: TransactionScreenEvent) = viewModelScope.launch {
        when (event) {
            TransactionScreenEvent.OnDonationRequestClick -> appNavigator.navigateTo(
                Destination.DonationDetails(_uiState.value.transactionView.donationRequestView?.id ?: "")
            )

            TransactionScreenEvent.OnMedicineClick -> appNavigator.navigateTo(
                Destination.MedicineDetails(_uiState.value.transactionView.medicine.id)
            )

            TransactionScreenEvent.OnReceiverClick -> Unit
            TransactionScreenEvent.OnSenderClick -> Unit
        }
    }


}