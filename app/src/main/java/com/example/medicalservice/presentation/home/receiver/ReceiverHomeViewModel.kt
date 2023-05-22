package com.example.medicalservice.presentation.home.receiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.diagnosis.GetUserLatestDiagnosisUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.user.GetCurrentUserUseCase
import com.example.model.app.transaction.TransactionView
import com.example.model.app.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReceiverHomeViewModel(
    private val getCurrentUserTransactionsUseCase: GetCurrentUserTransactionsUseCase,
    private val getUserLatestDiagnosisUseCase: GetUserLatestDiagnosisUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {
    private lateinit var transactionPager: Flow<PagingData<TransactionView>>

    private val _uiState = MutableStateFlow(ReceiverHomeScreenState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            transactionPager = Pager(
                config = PagingConfig(pageSize = 10),
                pagingSourceFactory = getCurrentUserTransactionsUseCase()
            ).flow.cachedIn(viewModelScope)
            _uiState.value = _uiState.value.copy(transactions = transactionPager)
        }
        viewModelScope.launch {
            getUserLatestDiagnosisUseCase().distinctUntilChanged().collect {
                _uiState.value = _uiState.value.copy(latestDiagnosisResult = it)
            }
        }
        viewModelScope.launch {
            getCurrentUserUseCase().distinctUntilChanged().collect {
                if (it !is User.Receiver) return@collect
                _uiState.value = _uiState.value.copy(user = it)
            }
        }
    }

    fun handleEvent(event: ReceiverHomeScreenEvent) = viewModelScope.launch {
        when (event) {
            ReceiverHomeScreenEvent.OnCreateDiagnosisRequestClicked -> appNavigator.navigateTo(
                Destination.DiagnosisForm()
            )

            ReceiverHomeScreenEvent.OnDiagnosisClicked -> appNavigator.navigateTo(
                Destination.DiagnosisDetails(
                    _uiState.value.latestDiagnosisResult?.id ?: ""
                )
            )

            ReceiverHomeScreenEvent.OnFAQClicked -> Unit
            ReceiverHomeScreenEvent.OnFeedbackClicked -> Unit
            is ReceiverHomeScreenEvent.OnMedicineClicked -> appNavigator.navigateTo(
                Destination.MedicineDetails(
                    event.medicineId
                )
            )

            is ReceiverHomeScreenEvent.OnQueryChanged -> _uiState.value =
                _uiState.value.copy(query = event.query)

            ReceiverHomeScreenEvent.OnSearchIconClicked -> _uiState.value =
                _uiState.value.copy(isSearchVisible = !_uiState.value.isSearchVisible)

            is ReceiverHomeScreenEvent.OnTransactionClicked -> appNavigator.navigateTo(
                Destination.TransactionDetails(
                    event.transactionId
                )
            )
        }
    }
}