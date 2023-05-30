package com.example.medicalservice.presentation.home.donner

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.example.medicalservice.presentation.components.HorizontalDonationRequestsList
import com.example.medicalservice.presentation.components.TransactionItem
import com.example.medicalservice.presentation.components.VerticalTransactionsList
import com.example.model.app.donation.dummyDonationRequests
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.empty
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun DonnerHomeScreen(
    viewModel: DonnerHomeViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DonnerHomeScreenContent(
        state = state,
        onEvent = viewModel::handleEvent,
    )

}

@Composable
private fun DonnerHomeScreenContent(
    state: DonnerHomeState,
    onEvent: (DonnerHomeScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HorizontalDonationRequestsList(
            donationRequestViewPagingData = state.donationRequestViews,
            title = "Urgent Donations",
            onDonationRequestClick = { onEvent(DonnerHomeScreenEvent.OnDonationRequestClick(it.id)) },
            onBookmarkClick = { onEvent(DonnerHomeScreenEvent.OnDonationRequestBookmarkClick(it)) },
            onSeeAllClick = { onEvent(DonnerHomeScreenEvent.OnSeeAllDonationRequestsClick) }
        )

        VerticalTransactionsList(
            transactionViews = state.transactionViews,
            onTransactionClick = { onEvent(DonnerHomeScreenEvent.OnTransactionClick(it)) },
            modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp / 2).dp),
            onMedicineClick = { onEvent(DonnerHomeScreenEvent.OnMedicineClick(it)) },
            title = "Recent Transactions"
        )
    }
}

@Composable
private fun VerticalTransactionsList(
    transactionViews: List<TransactionView>,
    onTransactionClick: (TransactionView) -> Unit,
    modifier: Modifier = Modifier,
    onMedicineClick: (String) -> Unit,
    title: String,
) {
    Text(text = title, style = MaterialTheme.typography.headlineMedium)
    LazyColumn(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(transactionViews) {
            TransactionItem(
                transactionView = it,
                onClick = { onTransactionClick(it) },
                onMedicineClick = onMedicineClick
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DonnerHomeScreenPreview() {
    Box {
        DonnerHomeScreenContent(
            state = DonnerHomeState(
                donationRequestViews = flowOf(PagingData.from(dummyDonationRequests())),
                transactionViews =
                listOf(
                    TransactionView.empty().copy(medicine = MedicineView.paracetamol())
                ),
            ),
            onEvent = {}
        )
    }
}