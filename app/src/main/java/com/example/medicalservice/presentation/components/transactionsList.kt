package com.example.medicalservice.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

fun LazyListScope.transactionsList(
    transactionViews: List<com.example.model.app.TransactionView>,
    onTransactionClick: (com.example.model.app.TransactionView) -> Unit
) {
    item {
        Text(text = "Recent Transactions", style = MaterialTheme.typography.headlineSmall)
    }

    items(transactionViews) {
        TransactionItem(
            transactionView = it,
            onClick = { onTransactionClick(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun TransactionItem(
    transactionView: com.example.model.app.TransactionView,
    onClick: () -> Unit
) {
    val simpleDateFormat by remember {
        mutableStateOf(SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()))
    }
    OutlinedCard(
        onClick = onClick,
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column {
                Text(text = "Medicine: ${transactionView.medicine.name}")
                Text(text = "Quantity: ${transactionView.quantity}")
            }
            Column {
                Row {
                    Text(text = "Status: ")
                    Text(
                        text = transactionView.status.name,
                        color = transactionView.status.color()
                    )
                }
                Text(text = "Issue Date: ${simpleDateFormat.format(transactionView.createdAt)}")
            }
        }
    }
}