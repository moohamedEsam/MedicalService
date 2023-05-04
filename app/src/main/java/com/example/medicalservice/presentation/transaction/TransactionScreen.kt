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
import com.example.model.app.empty
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionScreen(
    transactionView: com.example.model.app.TransactionView,
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 8.dp,
    onMedicineClick: (String) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(verticalSpacing)) {
        TransactionHeader(transactionView = transactionView)
        TransactionBody(transactionView = transactionView, onMedicineClick = onMedicineClick)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TransactionHeader(transactionView: com.example.model.app.TransactionView) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TransactionItem(
            label = "Sender:",
            value = transactionView.senderName,
            onClick = {}
        )

        TransactionItem(
            label = "Receiver:",
            value = transactionView.receiverName,
            onClick = {}
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TransactionBody(
    transactionView: com.example.model.app.TransactionView,
    onMedicineClick: (String) -> Unit
) {
    val simpleDateFormat by remember {
        mutableStateOf(SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()))
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TransactionItem(
            label = "Medicine",
            value = transactionView.medicine.name,
            onClick = { onMedicineClick(transactionView.medicine.id) }
        )

        TransactionItem(
            label = "Status:",
            value = transactionView.status.name,
            onClick = {
                //todo: show reason
            }
        )
    }
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = "Quantity: ${transactionView.quantity}")
        Text(text = "Created At: ${simpleDateFormat.format(transactionView.createdAt)}")
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
                com.example.model.app.TransactionView.empty().copy(
                    medicine = com.example.model.app.Medicine.empty().copy(name = "Paracetamol"),
                    quantity = 2,
                    status = com.example.model.app.TransactionView.Status.values().random(),
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