package com.example.medicalservice.presentation.donationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.domain.usecase.transaction.GetTransactionsUseCase
import com.example.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.domain.usecase.user.GetCurrentUserIdUseCase
import com.example.functions.snackbar.SnackBarManager
import com.example.model.app.donation.DonationRequestView
import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.toTransaction
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@KoinViewModel
class DonationListViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
    private val setDonationRequestBookmarkUseCase: SetDonationRequestBookmarkUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val appNavigator: AppNavigator,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val snackBarManager: SnackBarManager
) : ViewModel() {
    private var userId = ""
    private val pager = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = getDonationRequestsUseCase()
    ).flow.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(DonationListState(donationRequestViews = pager))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userId = getCurrentUserIdUseCase()
            val transactionPager = Pager(
                config = PagingConfig(pageSize = 10),
                pagingSourceFactory = getTransactionsUseCase(userId)
            ).flow.cachedIn(viewModelScope)
            _uiState.value = _uiState.value.copy(transactions = transactionPager)
        }

        viewModelScope.launch {
            _uiState.map { it.query }
                .debounce(200.milliseconds)
                .distinctUntilChanged()
                .collectLatest {
                    _uiState.value = _uiState.value.copy(
                        filteredTransactions = getFilteredTransactions(it),
                        filteredDonationRequestViews = getFilteredDonationRequestViews(it)
                    )
                }
        }
    }

    private fun getFilteredDonationRequestViews(query: String) =
        _uiState.value.donationRequestViews.map { pagingData ->
            pagingData.filter { donationRequestView ->
                donationRequestView.medicine.name.contains(query, ignoreCase = true)
            }
        }

    private fun getFilteredTransactions(query: String) =
        _uiState.value.transactions.map { pagingData ->
            pagingData.filter { transaction ->
                transaction.medicine.name.contains(query, ignoreCase = true) ||
                        transaction.medicine.diseases.any { disease ->
                            disease.name.contains(
                                query,
                                ignoreCase = true
                            )
                        }
            }
        }

    fun handleEvent(event: DonationListScreenEvent): Job = viewModelScope.launch {
        when (event) {
            is DonationListScreenEvent.OnDonationRequestBookmarkClick -> onBookmarkClick(event.donationRequestView)
            is DonationListScreenEvent.OnDonationRequestClick -> appNavigator.navigateTo(
                Destination.DonationDetails(
                    event.donationRequestView.id
                )
            )

            is DonationListScreenEvent.OnMedicineClick -> appNavigator.navigateTo(
                Destination.MedicineDetails(
                    event.medicineId
                )
            )

            is DonationListScreenEvent.OnTransactionClick -> _uiState.value = _uiState.value.copy(
                isConfirmationDialogVisible = true,
                selectedTransactionView = event.transactionView
            )

            DonationListScreenEvent.ConfirmationDialogEvent.OnCancelClick -> _uiState.value =
                _uiState.value.copy(
                    isConfirmationDialogVisible = false,
                    selectedTransactionView = null
                )

            DonationListScreenEvent.ConfirmationDialogEvent.OnConfirmClick -> {
                _uiState.value.selectedTransactionView?.let {
                    val result = updateTransactionUseCase(
                        it.toTransaction().copy(
                            status = TransactionView.Status.InProgress,
                            senderId = userId,
                            updatedAt = Date()
                        )
                    )
                    _uiState.value = _uiState.value.copy(isConfirmationDialogVisible = false)
                    val snackBarEvent = result.getSnackBarEvent(
                        successMessage = "Donation Successfully Confirmed",
                        successAction = { appNavigator.navigateTo(Destination.TransactionDetails(it.id)) },
                        successActionLabel = "View",
                        errorAction = { handleEvent(DonationListScreenEvent.ConfirmationDialogEvent.OnConfirmClick) },
                        errorActionLabel = "Retry"
                    )
                    snackBarManager.showSnackBarEvent(snackBarEvent)
                }
            }

            is DonationListScreenEvent.OnQueryChange -> _uiState.value =
                _uiState.value.copy(query = event.query)
        }
    }

    private fun onBookmarkClick(donationRequestView: DonationRequestView) = viewModelScope.launch {
        setDonationRequestBookmarkUseCase(donationRequestView.id, !donationRequestView.isBookmarked)
    }
}