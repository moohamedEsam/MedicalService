package com.example.medicalservice.presentation.donation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetDonationRequestsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonationViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
    private val initialDonationRequestId: String? = null,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(DonationScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.value = _uiState.value.copy(donationRequests = getDonationRequestsUseCase())
            if (initialDonationRequestId?.isNotBlank() == true)
                handleEvent(DonationScreenEvent.OnDonationRequestSelected(initialDonationRequestId))
        }
    }

    fun handleEvent(event: DonationScreenEvent) = viewModelScope.launch {
        when (event) {
            is DonationScreenEvent.OnDonationRequestSelected -> _uiState.value =
                _uiState.value.copy(selectedDonationRequestId = event.donationRequestId)

            is DonationScreenEvent.OnQueryChange -> onQueryChange(event.query)
            is DonationScreenEvent.OnChooseAnotherDonationRequest -> _uiState.value =
                _uiState.value.copy(selectedDonationRequestId = null, quantity = "")

            is DonationScreenEvent.OnDonateClick -> onDonateClick()
            is DonationScreenEvent.OnQuantityChange -> _uiState.value = _uiState.value.copy(quantity = event.quantity)

        }
    }

    private fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }


    private fun onDonateClick() {

    }
}