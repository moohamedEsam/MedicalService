package com.example.medicalservice.presentation.donation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.common.models.SnackBarEvent
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.donationRequest.GetDonationRequestByIdUseCase
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.domain.usecase.transaction.CreateTransactionUseCase
import com.example.functions.snackbar.SnackBarManager
import com.example.model.app.Transaction
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.UUID

@KoinViewModel
class DonationViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
    private val getDonationRequestByIdUseCase: GetDonationRequestByIdUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val setDonationRequestBookmarkUseCase: SetDonationRequestBookmarkUseCase,
    private val snackBarManager: SnackBarManager,
    private val appNavigator: AppNavigator,
    private val initialDonationRequestId: String? = null,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    private val donationRequests = Pager(
        config = PagingConfig(pageSize = 10),
    ) {
        getDonationRequestsUseCase()
    }.flow.cachedIn(viewModelScope)

    private val _uiState =
        MutableStateFlow(DonationScreenState(donationRequestViews = donationRequests))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (initialDonationRequestId?.isNotBlank() == false) return@launch
            val donationRequest = getDonationRequestByIdUseCase(initialDonationRequestId!!).first()
            handleEvent(DonationScreenEvent.OnDonationRequestSelected(donationRequest))
        }
    }

    fun handleEvent(event: DonationScreenEvent) = viewModelScope.launch {
        when (event) {
            is DonationScreenEvent.OnDonationRequestSelected -> _uiState.value =
                _uiState.value.copy(selectedDonationRequest = event.donationRequest)

            is DonationScreenEvent.OnQueryChange -> onQueryChange(event.query)
            is DonationScreenEvent.OnChooseAnotherDonationRequest -> _uiState.value =
                _uiState.value.copy(selectedDonationRequest = null, quantity = "")

            is DonationScreenEvent.OnDonateClick -> onDonateClick()
            is DonationScreenEvent.OnQuantityChange -> _uiState.value =
                _uiState.value.copy(quantity = event.quantity)

            is DonationScreenEvent.OnDonationRequestBookmarkClick -> setDonationRequestBookmarkUseCase(event.donationRequest.id, !event.donationRequest.isBookmarked)
            DonationScreenEvent.OnMedicineReadMoreClick -> {
                if (_uiState.value.selectedDonationRequest == null) return@launch
                appNavigator.navigateTo(Destination.MedicineDetails(_uiState.value.selectedDonationRequest!!.medicine.id))
            }
        }
    }

    private fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }


    private fun onDonateClick(): Job = viewModelScope.launch {
        if (!_uiState.value.isDonateButtonEnabled) return@launch
        if (_uiState.value.selectedDonationRequest == null) return@launch
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

    private fun createTransactionFromCurrentUIState() = Transaction(
        medicineId = _uiState.value.selectedDonationRequest!!.medicine.id,
        quantity = _uiState.value.quantity.toInt(),
        receiverId = UUID.randomUUID().toString(),
        receiverName = "Medical Service",
        senderId = UUID.randomUUID().toString(),
        senderName = "mohamed",
    )
}