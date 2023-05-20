package com.example.medicalservice.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.composecomponents.loadStateItem
import com.example.medicalservice.R
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.empty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun VerticalTransactionsList(
    transactionViewsFlow: Flow<PagingData<TransactionView>>,
    modifier: Modifier = Modifier,
    onTransactionClick: (TransactionView) -> Unit,
    onMedicineClick: (String) -> Unit,
    title: String
) {
    val transactionViews = transactionViewsFlow.collectAsLazyPagingItems()
    val dateFormatter by remember {
        mutableStateOf(SimpleDateFormat("MMMM dd", Locale.getDefault()))
    }
    Text(text = title, style = MaterialTheme.typography.headlineSmall)
    LazyColumn(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (transactionViews.itemCount == 0) {
            item {
                Text(
                    text = "No transactions yet",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            return@LazyColumn
        }
        items(transactionViews) { transaction ->
            if (transaction == null) return@items
            TransactionItem(
                transactionView = transaction,
                onClick = { onTransactionClick(transaction) },
                dateFormat = dateFormatter,
                onMedicineClick = { onMedicineClick(it) }
            )
        }
//        loadStateItem(transactionViews.loadState, spacerModifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItem(
    transactionView: TransactionView,
    modifier: Modifier = Modifier,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit,
    onMedicineClick: (String) -> Unit
) {
    /*Card(modifier = modifier.fillMaxWidth(), onClick = onClick) {
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


//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {
//                Text(
//                    text = "Quantity: ${transactionView.quantity}",
//                    style = MaterialTheme.typography.bodyLarge
//                )
//                Text(
//                    text = buildAnnotatedString {
//                        append("Status: ")
//                        withStyle(SpanStyle(transactionView.status.color())) {
//                            append(transactionView.status.name)
//                        }
//                    },
//                    style = MaterialTheme.typography.bodyLarge,
//                )
//            }
//
//            Text(
//                text = "Donation Date: ${dateFormat.format(transactionView.createdAt)}",
//                style = MaterialTheme.typography.bodyLarge
//            )
        }
    }*/

    ListItem(
        headlineText = {
            Text(
                text = transactionView.medicine.name,
                modifier = Modifier.clickable { onMedicineClick(transactionView.medicine.id) })
        },
        supportingText = { Text(text = "Quantity: ${transactionView.quantity}") },
        trailingContent = { Text(text = transactionView.status.name) },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.transaction),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = modifier
            .clip(RoundedCornerShape(percent = 10))
            .shadow(16.dp, RoundedCornerShape(percent = 10))
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
private fun VerticalTransactionsListPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TransactionItem(
            transactionView = TransactionView.empty().copy(medicine = MedicineView.paracetamol()),
            dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault()),
            onClick = { },
            onMedicineClick = {}
        )
    }

}