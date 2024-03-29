package com.example.medicalservice.presentation.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.transaction.GetTransactionDetailsUseCase
import com.example.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.domain.usecase.user.CallPhoneNumberUseCase
import com.example.model.app.transaction.Transaction
import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.toTransaction
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
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val callPhoneNumberUseCase: CallPhoneNumberUseCase,
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
            TransactionScreenEvent.OnMedicineClick -> appNavigator.navigateTo(
                Destination.MedicineDetails(_uiState.value.transactionView.medicine.id)
            )

            TransactionScreenEvent.OnReceiverClick -> _uiState.value =
                _uiState.value.copy(isUserDialogVisible = true, showSender = false)

            TransactionScreenEvent.OnSenderClick -> _uiState.value =
                _uiState.value.copy(isUserDialogVisible = true, showSender = true)

            TransactionScreenEvent.OnMarkAsDeliveredClick -> {
                val transaction = getUpdatedTransaction()
                updateTransactionUseCase(transaction)
            }

            TransactionScreenEvent.OnUserDialogDismiss -> _uiState.value =
                _uiState.value.copy(isUserDialogVisible = false)

            is TransactionScreenEvent.OnCallClick -> callPhoneNumberUseCase(event.phoneNumber)
            is TransactionScreenEvent.OnLocationClick -> appNavigator.navigateTo(
                Destination.Map(
                    event.latitude,
                    event.longitude
                )
            )

            TransactionScreenEvent.OnDeleteClick -> Unit // todo implement
            TransactionScreenEvent.OnEditClick -> Unit // todo implement
        }
    }

    private fun getUpdatedTransaction(): Transaction {
        var transaction = _uiState.value.transactionView.toTransaction()
        transaction = if(_uiState.value.transactionView.sender?.id == _uiState.value.user.id)
            transaction.copy(isDelivered = true)
        else
            transaction.copy(isReceived = true)

        if (transaction.isDelivered && transaction.isReceived)
            transaction = transaction.copy(status = TransactionView.Status.Completed)
        return transaction
    }


}