package com.example.medicalservice.presentation.donationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.domain.usecase.GetDonationRequestsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DonationListViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
) : ViewModel() {

    private val pager = Pager(
        config = PagingConfig(pageSize = 10),
    ){
        getDonationRequestsUseCase()
    }.flow.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(DonationListState(donationRequestViews = pager))
    val uiState = _uiState.asStateFlow()
}