package com.example.medicalservice.presentation.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.app.donation.DonationRequestView
import com.example.model.app.donation.empty
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import com.example.model.app.transaction.TransactionView
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionDetailsScreen(
    id: String,
    viewModel: TransactionViewModel = koinViewModel { parametersOf(id) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TransactionDetailsScreen(state = uiState, onEvent = viewModel::handleEvent)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TransactionDetailsScreen(
    state: TransactionScreenState,
    onEvent: (TransactionScreenEvent) -> Unit,
    dateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (state.transactionView.donationRequestView != null)
            TextButton(onClick = { onEvent(TransactionScreenEvent.OnDonationRequestClick) }) {
                Text(text = "View Request")
            }

        Text(
            text = "Transaction ID: ${state.transactionView.id}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = state.transactionView.medicine.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onEvent(TransactionScreenEvent.OnMedicineClick) }
        )


        Text(
            text = "Quantity: ${state.transactionView.quantity}",
            style = MaterialTheme.typography.bodyLarge
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Receiver: ${state.transactionView.receiverName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { onEvent(TransactionScreenEvent.OnReceiverClick) }
            )

            Text(
                text = "Sender: ${state.transactionView.senderName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { onEvent(TransactionScreenEvent.OnSenderClick) }
            )

        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Status: ${state.transactionView.status.name}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = "Created at: ${dateFormat.format(state.transactionView.createdAt)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

        }

        if (state.user.id == state.transactionView.senderId)
            Button(onClick = { }, modifier = Modifier.align(Alignment.End)) {
                Text(text = "Cancel transaction")
            }
        else
            Button(onClick = { }, modifier = Modifier.align(Alignment.End)) {
                Text(text = "I received the medicine")
            }
    }
}


@Preview(showBackground = true)
@Composable
private fun TransactionScreenPreview() {
    Surface {
        TransactionDetailsScreen(
            state = TransactionScreenState(
                transactionView = TransactionView(
                    id = "1",
                    createdAt = Date(),
                    updatedAt = Date(),
                    medicine = MedicineView.paracetamol(),
                    quantity = 1,
                    receiverId = "1",
                    receiverName = "mohamed",
                    senderId = "2",
                    senderName = "ahmed",
                    status = TransactionView.Status.Pending,
                    donationRequestView = DonationRequestView.empty()
                )
            ),
            onEvent = {}
        )
    }

}