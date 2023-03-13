package com.example.medicalservice.presentation.components

import androidx.compose.foundation.layout.Column
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
import com.example.models.Transaction
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import java.text.SimpleDateFormat
import java.util.*

fun LazyListScope.transactionsList(
    transactions: List<Transaction>,
    onTransactionClick: (Transaction) -> Unit
) {
    item {
        Text(text = "Recent Transactions", style = MaterialTheme.typography.headlineSmall)
    }

    items(transactions) {
        TransactionItem(
            transaction = it,
            onClick = { onTransactionClick(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionItem(
    transaction: Transaction,
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
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 4.dp,
            mainAxisAlignment = MainAxisAlignment.SpaceBetween
        ) {
            Column {
                Text(text = "Medicine: ${transaction.medicine.name}")
                Text(text = "Quantity: ${transaction.quantity}")
            }
            Column {
                Row {
                    Text(text = "Status: ")
                    Text(
                        text = transaction.status.name,
                        color = transaction.status.color()
                    )
                }
                Text(text = "Issue Date: ${simpleDateFormat.format(transaction.createdAt)}")
            }
        }
    }
}