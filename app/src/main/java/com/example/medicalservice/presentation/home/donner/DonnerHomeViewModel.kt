package com.example.medicalservice.presentation.home.donner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.user.GetCurrentUserIdUseCase
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.model.app.transaction.TransactionView
import com.example.model.app.user.User
import com.example.model.app.user.emptyDonor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonnerHomeViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
    private val getCurrentUserTransactionsUseCase: GetCurrentUserTransactionsUseCase,
    private val setDonationRequestBookmarkUseCase: SetDonationRequestBookmarkUseCase,
    private val appNavigator: AppNavigator,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val donationPager = Pager(
        config = PagingConfig(
            pageSize = 10
        ),
    ) {
        getDonationRequestsUseCase()
    }.flow.cachedIn(viewModelScope)

    private lateinit var transactionPager: Flow<PagingData<TransactionView>>

    private val _uiState = MutableStateFlow(
        DonnerHomeState(donationRequestViews = donationPager)
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val userId = getCurrentUserIdUseCase()
            transactionPager = Pager(
                config = PagingConfig(
                    pageSize = 10
                ),
            ) {
                getCurrentUserTransactionsUseCase(userId)
            }.flow.cachedIn(viewModelScope)

            _uiState.value = _uiState.value.copy(transactionViews = transactionPager)
        }
    }

    fun handleEvent(event: DonnerHomeScreenEvent) = viewModelScope.launch {
        when (event) {
            is DonnerHomeScreenEvent.OnQueryChange ->
                _uiState.value = _uiState.value.copy(query = event.query)

            is DonnerHomeScreenEvent.OnDonationRequestBookmarkClick -> {
                val donationRequest = event.donationRequest
                setDonationRequestBookmarkUseCase(donationRequest.id, !donationRequest.isBookmarked)
            }
            is DonnerHomeScreenEvent.OnDonationRequestClick -> appNavigator.navigateTo(Destination.DonationDetails(event.donationRequestId))
            is DonnerHomeScreenEvent.OnMedicineClick -> appNavigator.navigateTo(Destination.MedicineDetails(event.medicineId))
            is DonnerHomeScreenEvent.OnTransactionClick -> appNavigator.navigateTo(Destination.TransactionDetails(event.transactionView.id))
            DonnerHomeScreenEvent.OnSeeAllDonationRequestsClick -> appNavigator.navigateTo(Destination.DonationsList())
        }
    }
}