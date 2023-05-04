package com.example.medicalservice.presentation.home.donner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composecomponents.textField.OutlinedSearchTextField
import com.example.medicalservice.presentation.components.UrgentDonationList
import com.example.medicalservice.presentation.components.color
import com.example.model.app.empty
import com.example.model.app.emptyDonor
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DonnerHomeScreen(
    onMedicineClick: (String) -> Unit,
    onDonateClick: (String?) -> Unit,
    onTransactionClick: (String) -> Unit,
    viewModel: DonnerHomeViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DonnerHomeScreenContent(
        state = state,
        onEvent = viewModel::handleEvent,
        onMedicineClick = onMedicineClick,
        onDonateClick = onDonateClick,
        onTransactionClick = onTransactionClick
    )

}

@Composable
private fun DonnerHomeScreenContent(
    state: DonnerHomeState,
    onEvent: (DonnerHomeScreenEvent) -> Unit,
    onMedicineClick: (String) -> Unit,
    onDonateClick: (String?) -> Unit,
    onTransactionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        OutlinedSearchTextField(
            query = state.query,
            onQueryChange = { onEvent(DonnerHomeScreenEvent.OnQueryChange(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        UrgentDonationList(
            donationRequestViews = state.donationRequestViews,
            title = "Urgent Donations",
            onDonationRequestClick = { onDonateClick(it.id) },
        )

        RecentTransactions(
            transactionViews = state.transactionViews,
            onTransactionClick = { onTransactionClick(it.id) },
            modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp / 2).dp),
            onMedicineClick = onMedicineClick
        )
    }
}

@Composable
private fun DonnerScreenHeader(
    user: com.example.model.app.User.Donor
) {
    Text(text = "Hello ${user.username}", style = MaterialTheme.typography.headlineSmall)
}

@Composable
private fun RecentTransactions(
    transactionViews: List<com.example.model.app.TransactionView>,
    modifier: Modifier = Modifier,
    onTransactionClick: (com.example.model.app.TransactionView) -> Unit,
    onMedicineClick: (String) -> Unit
) {
    val dateFormatter by remember {
        mutableStateOf(SimpleDateFormat("MMMM dd", Locale.getDefault()))
    }
    Text(text = "Recent Transactions", style = MaterialTheme.typography.headlineSmall)
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(transactionViews) { transaction ->
            TransactionItem(
                transactionView = transaction,
                onClick = { onTransactionClick(transaction) },
                dateFormat = dateFormatter,
                onMedicineClick = { onMedicineClick(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionItem(
    transactionView: com.example.model.app.TransactionView,
    modifier: Modifier = Modifier,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit,
    onMedicineClick: (String) -> Unit
) {
    Card(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = transactionView.medicine.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onMedicineClick(transactionView.medicine.id) }
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Quantity: ${transactionView.quantity}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = buildAnnotatedString {
                        append("Status: ")
                        withStyle(SpanStyle(transactionView.status.color())) {
                            append(transactionView.status.name)
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Text(
                text = "Donation Date: ${dateFormat.format(transactionView.createdAt)}",
                style = MaterialTheme.typography.bodyLarge
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
                donationRequestViews = com.example.model.app.dummyDonationRequests(),
                user = com.example.model.app.User.emptyDonor().copy(username = "mohamed"),
                transactionViews = listOf(
                    com.example.model.app.TransactionView.empty().copy(
                        medicine = com.example.model.app.Medicine.empty().copy(name = "Panadol"),
                        receiverName = "Medical Service",
                        quantity = 10,
                        status = com.example.model.app.TransactionView.Status.Delivered,
                        donationRequestView = com.example.model.app.dummyDonationRequests().random()
                    )
                )
            ),
            onTransactionClick = {},
            onDonateClick = {},
            onMedicineClick = {},
            onEvent = {}
        )
    }
}