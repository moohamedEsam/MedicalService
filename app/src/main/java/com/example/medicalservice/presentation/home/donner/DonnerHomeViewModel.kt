package com.example.medicalservice.presentation.home.donner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicalservice.domain.GetCurrentUserTransactionsUseCase
import com.example.medicalservice.domain.GetCurrentUserUseCase
import com.example.medicalservice.domain.GetDonationRequestsUseCase
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
    private val getCurrentUserTransactionsUseCase: GetCurrentUserTransactionsUseCase,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    private val _uiState = MutableStateFlow(DonnerHomeState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            val user = getCurrentUserUseCase()
            val donationRequests = getDonationRequestsUseCase()
            val transactions = getCurrentUserTransactionsUseCase()
            _uiState.value = DonnerHomeState(
                user = user as? User.Donor ?: User.emptyDonor(),
                donationRequests = donationRequests,
                transactions = transactions,
            )
        }
    }

    fun handleEvent(event: DonnerHomeScreenEvent) = viewModelScope.launch {
        when (event) {
            is DonnerHomeScreenEvent.OnQueryChange ->
                _uiState.value = _uiState.value.copy(query = event.query)
        }
    }
}