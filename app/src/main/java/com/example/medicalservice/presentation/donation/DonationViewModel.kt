package com.example.medicalservice.presentation.donation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.domain.usecase.GetDonationRequestByIdUseCase
import com.example.domain.usecase.GetDonationRequestsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonationViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
    private val getDonationRequestByIdUseCase: GetDonationRequestByIdUseCase,
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

        }
    }

    private fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }


    private fun onDonateClick() {

    }
}