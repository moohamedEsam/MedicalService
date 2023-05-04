package com.example.medicalservice.presentation.donationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetDonationRequestsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonationListViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DonationListState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = DonationListState(donationRequests = getDonationRequestsUseCase())
        }
    }
}