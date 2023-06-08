package com.example.medicalservice.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.model.app.donation.DonationRequestView
import com.example.model.app.transaction.TransactionView

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun DonationRequestAndTransactionsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    donationRequests: LazyPagingItems<DonationRequestView>,
    onDonationRequestClick: (DonationRequestView) -> Unit,
    transactions: LazyPagingItems<TransactionView>,
    onTransactionClick: (TransactionView) -> Unit,
    onMedicineClick: (String) -> Unit,
) {
    var active by remember {
        mutableStateOf(false)
    }
    DockedSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onQueryChange,
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text("Search") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            items(donationRequests, key = { it.id }) {
                if (it == null) return@items
                ListItem(
                    headlineContent = { Text(it.medicine.name) },
                    supportingContent = {
                        Text(
                            it.medicine.uses.firstOrNull() ?: "",
                            maxLines = 1
                        )
                    },
                    trailingContent = {
                        Badge(content = { Text((it.needed - it.collected).toString()) })
                    },
                    modifier = Modifier
                        .clickable { onDonationRequestClick(it) }
                        .animateItemPlacement(),
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                )
            }
            items(transactions, key = { it.id }) {
                if (it == null) return@items
                TransactionItem(
                    transactionView = it,
                    onClick = { onTransactionClick(it) },
                    onMedicineClick = { medicineId -> onMedicineClick(medicineId) },
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}