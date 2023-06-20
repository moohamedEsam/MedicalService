package com.example.medicalservice.presentation.donation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.models.SnackBarEvent
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.donationRequest.GetDonationRequestByIdUseCase
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.domain.usecase.user.GetCurrentUserIdUseCase
import com.example.functions.snackbar.SnackBarManager
import com.example.model.app.transaction.Transaction
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonationViewModel(
    private val getDonationRequestByIdUseCase: GetDonationRequestByIdUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val snackBarManager: SnackBarManager,
    private val appNavigator: AppNavigator,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val donationRequestId: String,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(DonationScreenState())
    val uiState = _uiState.asStateFlow()
    private lateinit var userId : String
    init {
        viewModelScope.launch {
            userId = getCurrentUserIdUseCase()
            _uiState.value = _uiState.value.copy(userId = userId)
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            getDonationRequestByIdUseCase(donationRequestId).collectLatest {
                _uiState.value = _uiState.value.copy(donationRequest = it)
            }
        }
    }

    fun handleEvent(event: DonationScreenEvent) = viewModelScope.launch {
        when (event) {
            is DonationScreenEvent.OnDonateClick -> onDonateClick()
            is DonationScreenEvent.OnQuantityChange -> _uiState.value =
                _uiState.value.copy(quantity = event.quantity)

            DonationScreenEvent.OnMedicineReadMoreClick -> {
                appNavigator.navigateTo(Destination.MedicineDetails(_uiState.value.donationRequest.medicine.id))
            }

        }
    }


    private fun onDonateClick(): Job = viewModelScope.launch {
        if (!_uiState.value.isDonateButtonEnabled) return@launch
        val transaction = createTransactionFromCurrentUIState()
        val result = createTransactionUseCase(transaction)
        result.ifSuccess {
            val event = SnackBarEvent("Donation sent successfully", actionLabel = "View")
            snackBarManager.showSnackBarEvent(event)
        }
        result.ifFailure {
            val event = SnackBarEvent(
                it ?: "Failed to send donation",
                actionLabel = "Retry",
                action = ::onDonateClick
            )
            snackBarManager.showSnackBarEvent(event)
        }
    }

    private suspend fun createTransactionFromCurrentUIState() = Transaction(
        medicineId = _uiState.value.donationRequest.medicine.id,
        quantity = _uiState.value.quantity.toInt(),
        receiverId = "",
        senderId = userId,
        donationRequestId = _uiState.value.donationRequest.id
    )
}