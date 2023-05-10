package com.example.medicalservice.presentation.myDonations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composecomponents.textField.OutlinedSearchTextField
import com.example.medicalservice.presentation.components.HorizontalDonationRequestsList
import com.example.medicalservice.presentation.components.VerticalTransactionsList
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyDonationsScreen(
    viewModel: MyDonationsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MyDonationsScreen(
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
private fun MyDonationsScreen(
    state: MyDonationsScreenState,
    onEvent: (MyDonationsScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedSearchTextField(
            query = state.query,
            onQueryChange = { onEvent(MyDonationsScreenEvent.QueryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = "Search by name"
        )
        HorizontalDonationRequestsList(
            donationRequestViewPagingData = state.donationRequests,
            title = "Bookmarked Donations",
            onDonationRequestCardClick = { onEvent(MyDonationsScreenEvent.OnDonationRequestClick(it)) },
            onBookmarkClick = { onEvent(MyDonationsScreenEvent.OnDonationRequestBookmarkClick(it)) }
        )

        VerticalTransactionsList(
            transactionViewsFlow = state.transactions,
            onTransactionClick = { onEvent(MyDonationsScreenEvent.OnTransactionClick(it)) },
            onMedicineClick = { onEvent(MyDonationsScreenEvent.OnMedicineClick(it)) },
            title = "Transactions",
            modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp / 2).dp)
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun MyDonationScreenPreview() {
    Surface {
        MyDonationsScreen(
            state = MyDonationsScreenState(),
            onEvent = {}
        )
    }

}
