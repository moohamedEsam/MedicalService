package com.example.medicalservice.presentation.donationList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.composecomponents.loadStateItem
import com.example.medicalservice.presentation.components.UrgentDonationList
import com.example.model.app.DonationRequestView
import com.example.model.app.dummyDonationRequests
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun DonationListScreen(
    viewModel: DonationListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DonationListScreen(state)
}

@Composable
private fun DonationListScreen(
    state: DonationListState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DonationListTopBar(
            query = state.query,
            onQueryChange = { },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
                .shadow(8.dp),
        )
        DonationListContent(state.donationRequestViews)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonationListTopBar(
    query: String,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit = {}
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        label = { Text("Search by medicine or disease name ") },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
        colors = TextFieldDefaults.textFieldColors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Composable
private fun ColumnScope.DonationListContent(
    donationRequestViews: Flow<PagingData<DonationRequestView>>
) {
    UrgentDonationList(donationRequestViews, "Urgent Donations")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Featured Donations", style = MaterialTheme.typography.headlineSmall)
        TextButton(onClick = { }) {
            Text("See All")
        }
    }
    FeaturedDonationList(
        donationRequestViews,
        modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp / 2).dp)
    )
}


@Composable
private fun FeaturedDonationList(
    donationRequestViewsFlow: Flow<PagingData<DonationRequestView>>,
    modifier: Modifier = Modifier
) {
    val donationRequestViews = donationRequestViewsFlow.collectAsLazyPagingItems()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(donationRequestViews, key = { it.id }) {
            if (it == null) return@items
            VerticalDonationItem(
                donationRequestView = it,
                modifier = Modifier.fillMaxWidth()
            )
        }
        loadStateItem(donationRequestViews.loadState, spacerModifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VerticalDonationItem(
    donationRequestView: com.example.model.app.DonationRequestView,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    onDonateClick: () -> Unit = { }
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(donationRequestView.medicine.name, fontWeight = FontWeight.Bold)
            Text(donationRequestView.medicine.uses.firstOrNull()?:"", maxLines = 1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.Group, contentDescription = null)
                    Text("${donationRequestView.contributorsCount} Contributors")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.Timer, contentDescription = null)
                    val duration =
                        (donationRequestView.endDate - System.currentTimeMillis()).toDuration(
                            DurationUnit.DAYS
                        )
                    Text("ends in ${Random.nextInt(1, 100)} days")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DonationListScreenPreview() {
    Surface {
        DonationListScreen(
            state = DonationListState(
                donationRequestViews = flowOf(PagingData.from(dummyDonationRequests())),
                query = "paracetamol"
            )
        )
    }

}