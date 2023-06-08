package com.example.medicalservice.presentation.donation

import androidx.paging.testing.asPagingSourceFactory
import app.cash.turbine.test
import com.example.common.models.Result
import com.example.functions.snackbar.FakeSnackBarManager
import com.example.model.FakeAppNavigator
import com.example.model.app.donation.dummyDonationRequests
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class DonationViewModelTest {
    private lateinit var viewModel: DonationViewModel

    @OptIn(DelicateCoroutinesApi::class)
    private val thread = newSingleThreadContext("UI thread")
    private val dummyDonationRequests = dummyDonationRequests()
    private var dummyDonationRequestId: String? = null

    @Before
    fun setUp() {
        Dispatchers.setMain(thread)
        dummyDonationRequestId =
            if (Random.nextBoolean()) dummyDonationRequests.first().id else null
        viewModel = DonationViewModel(
            getDonationRequestsUseCase = {
                flowOf(dummyDonationRequests)
                    .asPagingSourceFactory(CoroutineScope(Dispatchers.Main))
                    .invoke()
            },
            initialDonationRequestId = dummyDonationRequestId,
            appNavigator = FakeAppNavigator(),
            coroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> },
            getDonationRequestByIdUseCase = { flowOf(dummyDonationRequests.first()) },
            createTransactionUseCase = { Result.Success(Unit) },
            setDonationRequestBookmarkUseCase = { _, _ -> Result.Success(Unit) },
            snackBarManager = FakeSnackBarManager()
        )
    }

    @Test
    fun `should set donation requests on init`() = runTest {
        assertThat(viewModel.uiState.value.donationRequestViews).isEqualTo(dummyDonationRequests)
    }

    @Test
    fun `should set selected donation request on init`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            if (dummyDonationRequestId != null) {
                assertThat(state.selectedDonationRequest).isNotNull()
                assertThat(state.selectedDonationRequest!!.id).isEqualTo(dummyDonationRequestId)
            } else
                assertThat(state.selectedDonationRequest).isNull()
        }
    }

    @Test
    fun `donate button should be disabled if no donation request is selected`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isDonateButtonEnabled).isFalse()
        }
    }

    @Test
    fun `donate button should be enabled if donation request and quantity are selected`() =
        runTest {
            viewModel.handleEvent(DonationScreenEvent.OnDonationRequestSelected(donationRequest = dummyDonationRequests.random()))
            viewModel.handleEvent(DonationScreenEvent.OnQuantityChange("1"))
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.isDonateButtonEnabled).isTrue()
            }
        }

    @After
    fun tearDown() {
        thread.close()
    }
}