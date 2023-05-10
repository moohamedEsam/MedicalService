package com.example.medicalservice.presentation.donationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.model.app.DonationRequestView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonationListViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
    private val setDonationRequestBookmarkUseCase: SetDonationRequestBookmarkUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val pager = Pager(
        config = PagingConfig(pageSize = 10),
    ) {
        getDonationRequestsUseCase()
    }.flow.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(DonationListState(donationRequestViews = pager))
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: DonationListScreenEvent)=viewModelScope.launch {
        when(event){
            is DonationListScreenEvent.OnDonationRequestBookmarkClick -> onBookmarkClick(event.donationRequestView)
            is DonationListScreenEvent.OnDonationRequestClick -> appNavigator.navigateTo(Destination.DonationDetails(event.donationRequestView.id))
        }
    }

    private fun onBookmarkClick(donationRequestView: DonationRequestView) = viewModelScope.launch {
        setDonationRequestBookmarkUseCase(donationRequestView.id, !donationRequestView.isBookmarked)
    }
}