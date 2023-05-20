package com.example.medicalservice.presentation.myDonations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.common.navigation.AppNavigator
import com.example.common.navigation.Destination
import com.example.domain.usecase.donationRequest.GetDonationRequestsUseCase
import com.example.domain.usecase.donationRequest.SetDonationRequestBookmarkUseCase
import com.example.domain.usecase.transaction.GetCurrentUserTransactionsUseCase
import com.example.domain.usecase.user.GetCurrentUserIdUseCase
import com.example.model.app.transaction.TransactionView
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
@KoinViewModel
class MyDonationsViewModel(
    private val getDonationRequestsUseCase: GetDonationRequestsUseCase,
    private val getCurrentUserTransactionsUseCase: GetCurrentUserTransactionsUseCase,
    private val setDonationRequestBookmarkUseCase: SetDonationRequestBookmarkUseCase,
    private val appNavigator: AppNavigator,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val donationPager = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { getDonationRequestsUseCase() })
        .flow
        .map { data -> data.filter { it.isBookmarked } }
        .distinctUntilChanged()
        .cachedIn(viewModelScope)

    private lateinit var transactionPager : Flow<PagingData<TransactionView>>

    private val _uiState = MutableStateFlow(MyDonationsScreenState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: MyDonationsScreenEvent) = viewModelScope.launch {
        when (event) {
            is MyDonationsScreenEvent.OnDonationRequestBookmarkClick ->
                setDonationRequestBookmarkUseCase(
                    event.donationRequest.id,
                    !event.donationRequest.isBookmarked
                )

            is MyDonationsScreenEvent.OnDonationRequestClick ->
                appNavigator.navigateTo(Destination.DonationDetails(event.donationRequest.id))

            is MyDonationsScreenEvent.OnMedicineClick -> appNavigator.navigateTo(
                Destination.MedicineDetails(
                    event.medicineId
                )
            )

            is MyDonationsScreenEvent.OnTransactionClick -> Unit
            is MyDonationsScreenEvent.QueryChanged -> _uiState.value =
                _uiState.value.copy(query = event.query)
        }
    }

    init {
        initializeTransactionPager()
        observeQueryChange()
    }

    private fun observeQueryChange() {
        viewModelScope.launch {
            _uiState.map { it.query }
                .debounce(0.5.seconds)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val transactions = transactionPager
                        .map { data ->
                            data.filter {
                                query.isBlank() || it.medicine.name.contains(query, true)
                                        || it.medicine.diseases.any { disease ->
                                    disease.name.contains(
                                        query,
                                        true
                                    )
                                }
                            }
                        }

                    val donationRequests = donationPager
                        .map { data ->
                            data.filter {
                                query.isBlank() || it.medicine.name.contains(query, true)
                                        || it.medicine.diseases.any { disease ->
                                    disease.name.contains(
                                        query,
                                        true
                                    )
                                }
                            }
                        }

                    _uiState.value = _uiState.value.copy(
                        transactions = transactions,
                        donationRequests = donationRequests
                    )
                }
        }
    }

    private fun initializeTransactionPager() {
        viewModelScope.launch {
            val userId = getCurrentUserIdUseCase()
            transactionPager = Pager(
                config = PagingConfig(pageSize = 10),
                pagingSourceFactory = { getCurrentUserTransactionsUseCase(userId) })
                .flow
                .cachedIn(viewModelScope)
        }
    }
}