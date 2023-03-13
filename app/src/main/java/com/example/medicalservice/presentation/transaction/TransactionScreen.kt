package com.example.medicalservice.presentation.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.models.Medicine
import com.example.models.Transaction
import com.example.models.empty
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionScreen(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 8.dp,
    onMedicineClick: (String) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(verticalSpacing)) {
        TransactionHeader(transaction = transaction)
        TransactionBody(transaction = transaction, onMedicineClick = onMedicineClick)
    }
}

@Composable
private fun TransactionHeader(transaction: Transaction) {
    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        TransactionItem(
            label = "Sender:",
            value = transaction.senderName,
            onClick = {}
        )

        TransactionItem(
            label = "Receiver:",
            value = transaction.receiverName,
            onClick = {}
        )
    }

}

@Composable
private fun TransactionBody(
    transaction: Transaction,
    onMedicineClick: (String) -> Unit
) {
    val simpleDateFormat by remember {
        mutableStateOf(SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()))
    }

    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        crossAxisAlignment = FlowCrossAxisAlignment.Center
    ) {
        TransactionItem(
            label = "Medicine",
            value = transaction.medicine.name,
            onClick = { onMedicineClick(transaction.medicine.id) }
        )

        TransactionItem(
            label = "Status:",
            value = transaction.status.name,
            onClick = {
                //todo: show reason
            }
        )
    }
    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        crossAxisAlignment = FlowCrossAxisAlignment.Center
    ) {
        Text(text = "Quantity: ${transaction.quantity}")
        Text(text = "Created At: ${simpleDateFormat.format(transaction.createdAt)}")
    }

}

@Composable
private fun TransactionItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(text = label)
        TextButton(onClick = onClick) {
            Text(text = value)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionScreenPreview() {
    Box(
     modifier= Modifier.fillMaxSize(),
     contentAlignment= Alignment.Center
    ){

    }
    Dialog(onDismissRequest = {}) {
        Card {
            TransactionScreen(
                Transaction.empty().copy(
                    medicine = Medicine.empty().copy(name = "Paracetamol"),
                    quantity = 2,
                    status = Transaction.Status.values().random(),
                    senderName = "ahmed",
                    receiverName = "mohamed",
                ),
                onMedicineClick = {},
                verticalSpacing = 4.dp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}