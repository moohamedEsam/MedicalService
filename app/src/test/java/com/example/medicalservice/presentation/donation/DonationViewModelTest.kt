package com.example.medicalservice.presentation.donation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val dummyDonationRequests = com.example.model.app.dummyDonationRequests()
    private var dummyDonationRequestId: String? = null

    @Before
    fun setUp() {
        Dispatchers.setMain(thread)
        dummyDonationRequestId = if (Random.nextBoolean()) dummyDonationRequests.first().id else null
        viewModel = DonationViewModel(
            getDonationRequestsUseCase = { dummyDonationRequests },
            initialDonationRequestId = dummyDonationRequestId,
            coroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }
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
                assertThat(state.selectedDonationRequestView).isNotNull()
                assertThat(state.selectedDonationRequestView!!.id).isEqualTo(dummyDonationRequestId)
            } else
                assertThat(state.selectedDonationRequestView).isNull()
        }
    }

    @Test
    fun `should set selected donation request on event`() = runTest {
        val newDonationRequestId = dummyDonationRequests.last().id
        viewModel.handleEvent(DonationScreenEvent.OnDonationRequestSelected(newDonationRequestId))
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedDonationRequestView).isNotNull()
            assertThat(state.selectedDonationRequestView!!.id).isEqualTo(newDonationRequestId)
        }
    }

    @Test
    fun `should set selected donation request to null if id not in donation requests`() = runTest {
        val newDonationRequestId = "random id"
        viewModel.handleEvent(DonationScreenEvent.OnDonationRequestSelected(newDonationRequestId))
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedDonationRequestView).isNull()
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
            val newDonationRequestId = dummyDonationRequests.last().id
            viewModel.handleEvent(DonationScreenEvent.OnDonationRequestSelected(newDonationRequestId))
            viewModel.handleEvent(DonationScreenEvent.OnQuantityChange(1))
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