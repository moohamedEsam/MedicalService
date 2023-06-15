package com.example.medicalservice.presentation.transaction

import android.Manifest
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicalservice.presentation.components.color
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import com.example.model.app.transaction.TransactionView
import com.example.model.app.user.User
import com.example.model.app.user.emptyDonor
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
        Text(
            text = "Transaction ID: ${state.transactionView.id}",
            style = MaterialTheme.typography.bodyMedium
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = state.transactionView.medicine.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onEvent(TransactionScreenEvent.OnMedicineClick) }
            )
            ActionRow(state, onEvent)
        }


        Text(
            text = "Quantity: ${state.transactionView.quantity}",
            style = MaterialTheme.typography.bodyLarge
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Receiver: ${state.transactionView.receiver?.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { onEvent(TransactionScreenEvent.OnReceiverClick) }
            )

            Text(
                text = "Sender: ${state.transactionView.sender?.username}",
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
                color = state.transactionView.status.color()
            )

            Text(
                text = "Created at: ${dateFormat.format(state.transactionView.createdAt)}",
                style = MaterialTheme.typography.bodyMedium
            )

        }
        if (state.transactionView.status != TransactionView.Status.Completed && state.isCompleteButtonVisible)
            Button(
                onClick = { onEvent(TransactionScreenEvent.OnMarkAsDeliveredClick) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Mark as delivered")
            }
    }
    if (state.isUserDialogVisible)
        UserDialog(
            user = if (state.showSender) state.transactionView.sender else state.transactionView.receiver,
            onEvent = onEvent
        )
}

@Composable
private fun ActionRow(state: TransactionScreenState, onEvent: (TransactionScreenEvent) -> Unit) {
    if (state.transactionView.status != TransactionView.Status.Pending) return
    Row {
        IconButton(onClick = { onEvent(TransactionScreenEvent.OnEditClick) }) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
        }

        IconButton(onClick = { onEvent(TransactionScreenEvent.OnDeleteClick) }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UserDialog(
    user: User?,
    onEvent: (TransactionScreenEvent) -> Unit
) {
    if (user == null) return
    val callPhonePermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it)
            onEvent(TransactionScreenEvent.OnCallClick(user.phone))
    }
    Dialog(onDismissRequest = { onEvent(TransactionScreenEvent.OnUserDialogDismiss) }) {
        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Phone: ${user.phone}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = { callPhonePermission.launch(Manifest.permission.CALL_PHONE) }) {
                        Icon(imageVector = Icons.Outlined.Call, contentDescription = null)
                    }
                }
                val address = Geocoder(LocalContext.current).getFromLocation(
                    user.location.latitude,
                    user.location.longitude,
                    1
                )?.firstOrNull()

                FlowRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Address: ${address?.getAddressLine(0)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = {
                        onEvent(
                            TransactionScreenEvent.OnLocationClick(
                                user.location.latitude,
                                user.location.longitude
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
                    }
                }

            }
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
                    status = TransactionView.Status.Pending,
                    sender = User.emptyDonor()
                        .copy(username = "mohamed esam", phone = "01000000000"),
                    receiver = null
                ),
                isUserDialogVisible = true,
                showSender = true
            ),
            onEvent = {}
        )
    }

}