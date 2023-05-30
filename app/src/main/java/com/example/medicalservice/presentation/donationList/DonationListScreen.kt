package com.example.medicalservice.presentation.donationList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.composecomponents.loadStateItem
import com.example.medicalservice.presentation.components.HorizontalDonationRequestsList
import com.example.medicalservice.presentation.components.TransactionItem
import com.example.model.app.donation.dummyDonationRequests
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.empty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun DonationListScreen(
    viewModel: DonationListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DonationListScreen(state = state, onEvent = viewModel::handleEvent)
}

@Composable
private fun DonationListScreen(
    state: DonationListState,
    onEvent: (DonationListScreenEvent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DonationListTopBar(
            query = state.query,
            onQueryChange = { },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
                .shadow(8.dp),
        )
        DonationListContent(state, onEvent)
    }
    if (!state.isConfirmationDialogVisible || state.selectedTransactionView == null) return
    AlertDialog(
        onDismissRequest = { onEvent(DonationListScreenEvent.ConfirmationDialogEvent.OnCancelClick) },
        confirmButton = {
            TextButton(
                onClick = { onEvent(DonationListScreenEvent.ConfirmationDialogEvent.OnConfirmClick) }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onEvent(DonationListScreenEvent.ConfirmationDialogEvent.OnCancelClick) }
            ) {
                Text("Cancel")
            }
        },
        title = { Text("Confirmation") },
        text = {
            val text = buildAnnotatedString {
                append("confirm donation for ")
                withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                    append(state.selectedTransactionView.quantity.toString())
                    append(" ")
                    append(state.selectedTransactionView.medicine.name)
                }

            }
            Text(text, style = MaterialTheme.typography.bodyLarge)
        },
        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        icon = {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonationListTopBar(
    query: String,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit = {}
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        label = { Text("Search by medicine or disease name ") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
        colors = TextFieldDefaults.textFieldColors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Composable
private fun DonationListContent(
    state: DonationListState,
    onEvent: (DonationListScreenEvent) -> Unit
) {
    HorizontalDonationRequestsList(
        donationRequestViewPagingData = state.donationRequestViews,
        title = "Urgent Donations",
        onDonationRequestCardClick = { onEvent(DonationListScreenEvent.OnDonationRequestClick(it)) },
        onDonationRequestClick = { onEvent(DonationListScreenEvent.OnDonationRequestClick(it)) },
        onBookmarkClick = { onEvent(DonationListScreenEvent.OnDonationRequestBookmarkClick(it)) },
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Featured Donations", style = MaterialTheme.typography.headlineSmall)
    }
    FeaturedDonationList(
        transactionsFlow = state.transactions,
        onEvent = onEvent,
        modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp / 2).dp)
    )
}


@Composable
private fun FeaturedDonationList(
    transactionsFlow: Flow<PagingData<TransactionView>>,
    onEvent: (DonationListScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val transactions = transactionsFlow.collectAsLazyPagingItems()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(transactions) {
            if (it == null) return@items
            TransactionItem(
                transactionView = it,
                onClick = { onEvent(DonationListScreenEvent.OnTransactionClick(it)) },
                onMedicineClick = { medicineId ->
                    onEvent(
                        DonationListScreenEvent.OnMedicineClick(
                            medicineId
                        )
                    )
                },
            )
        }
        loadStateItem(transactions.loadState, spacerModifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun DonationListScreenPreview() {
    Surface {
        DonationListScreen(
            state = DonationListState(
                donationRequestViews = flowOf(PagingData.from(dummyDonationRequests())),
                query = "paracetamol",
                isConfirmationDialogVisible = true,
                selectedTransactionView = TransactionView.empty()
                    .copy(medicine = MedicineView.paracetamol(), quantity = 10)
            )
        )
    }

}