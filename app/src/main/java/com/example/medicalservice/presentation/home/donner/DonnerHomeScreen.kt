package com.example.medicalservice.presentation.home.donner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.example.medicalservice.presentation.components.HorizontalDonationRequestsList
import com.example.medicalservice.presentation.components.VerticalTransactionsList
import com.example.model.app.user.User
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
            transactionViewsFlow = state.transactionViews,
            onTransactionClick = { onEvent(DonnerHomeScreenEvent.OnTransactionClick(it)) },
            modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp / 2).dp),
            onMedicineClick = { onEvent(DonnerHomeScreenEvent.OnMedicineClick(it)) },
            title = "Recent Transactions"
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DonnerHomeScreenPreview() {
    Box {
        DonnerHomeScreenContent(
            state = DonnerHomeState(),
            onEvent = {}
        )
    }
}