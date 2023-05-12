
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.medicalservice.presentation.transaction.TransactionScreenState
import com.example.model.app.donation.DonationRequestView
import com.example.model.app.donation.empty
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import com.example.model.app.transaction.TransactionView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionView(
    state: TransactionScreenState,
    dateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
) {
    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (state.transactionView.donationRequestView != null)
            TextButton(onClick = { }) {
                Text(text = "View Request")
            }

        Text(
            text = "Transaction ID: ${state.transactionView.id}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = state.transactionView.medicine.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
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
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = "Sender: ${state.transactionView.senderName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
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
        Box {

        }
        Dialog(onDismissRequest = {}) {
            Card {
                TransactionView(
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
                    )
                )
            }
        }
    }

}