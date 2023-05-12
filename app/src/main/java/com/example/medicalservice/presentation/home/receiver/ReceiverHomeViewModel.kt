package com.example.medicalservice.presentation.home.receiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.common.navigation.AppNavigator
import com.example.domain.usecase.diagnosis.GetUserLatestDiagnosisUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReceiverHomeViewModel(
    private val getCurrentUserTransactionsUseCase: GetCurrentUserTransactionsUseCase,
    private val getUserLatestDiagnosisUseCase: GetUserLatestDiagnosisUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {
    private val transactionPager = Pager(
        PagingConfig(pageSize = 10)
    ) {
        getCurrentUserTransactionsUseCase()
    }.flow.cachedIn(viewModelScope)

    private val _uiState =
        MutableStateFlow(ReceiverHomeScreenState(transactions = transactionPager))

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserLatestDiagnosisUseCase().distinctUntilChanged().collect {
                _uiState.value = _uiState.value.copy(latestDiagnosisResult = it)
            }
        }
    }

    fun handleEvent(event: ReceiverHomeScreenEvent) = viewModelScope.launch {

    }
}