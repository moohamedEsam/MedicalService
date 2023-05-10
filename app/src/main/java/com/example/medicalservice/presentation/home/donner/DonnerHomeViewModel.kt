package com.example.medicalservice.presentation.home.donner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.model.app.emptyDonor
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
    private val setDonationRequestBookmarkUseCase: SetDonationRequestBookmarkUseCase,
    private val appNavigator: AppNavigator,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ViewModel() {
    private val donationPager = Pager(
        config = PagingConfig(
            pageSize = 10
        ),
    ) {
        getDonationRequestsUseCase()
    }.flow.cachedIn(viewModelScope)

    private val transactionPager = Pager(
        config = PagingConfig(
            pageSize = 10,
        ),
    ) {
        getCurrentUserTransactionsUseCase()
    }.flow.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(
        DonnerHomeState(
            donationRequestViews = donationPager,
            transactionViews = transactionPager
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            val user = getCurrentUserUseCase()
            _uiState.value = _uiState.value.copy(
                user = user as? com.example.model.app.User.Donor
                    ?: com.example.model.app.User.emptyDonor(),
            )
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
            is DonnerHomeScreenEvent.OnTransactionClick -> Unit
            DonnerHomeScreenEvent.OnSeeAllDonationRequestsClick -> appNavigator.navigateTo(Destination.DonationsList())
        }
    }
}