package com.example.medicalservice.presentation.donationList

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicalservice.R
import com.example.models.app.DonationRequest
import com.example.models.app.dummyDonationRequests
import kotlinx.coroutines.delay
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
//            .verticalScroll(rememberScrollState())
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
        DonationListContent(state.donationRequests)
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
private fun DonationListContent(
    donationRequests: List<DonationRequest>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Urgent Donations", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        TextButton(onClick = { }) {
            Text("See All")
        }
    }
    UrgentDonationList(donationRequests)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Featured Donations", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        TextButton(onClick = { }) {
            Text("See All")
        }
    }
    FeaturedDonationList(donationRequests)
}

@Composable
private fun UrgentDonationList(donationRequests: List<DonationRequest>) {
    val listState = rememberLazyListState()
    val visibleItemIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    LaunchedEffect(key1 = visibleItemIndex) {
        Log.i("DonationListScreen", "UrgentDonationList: $visibleItemIndex")
        delay(3000)
        if (visibleItemIndex < donationRequests.size - 1)
            listState.animateScrollToItem(visibleItemIndex + 1)
        else
            listState.scrollToItem(0)

    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
    ) {
        items(donationRequests, key = { it.id }) {
            DonationListItem(
                donationRequest = it,
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.9f)
            )
        }
        item { Spacer(modifier = Modifier.width(16.dp)) }
    }
}

@Composable
private fun FeaturedDonationList(donationRequests: List<DonationRequest>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(donationRequests, key = { it.id }) {
            VerticalDonationItem(
                donationRequest = it,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VerticalDonationItem(
    donationRequest: DonationRequest,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    onDonateClick: () -> Unit = { }
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(donationRequest.medicine.name, fontWeight = FontWeight.Bold)
            Text(donationRequest.medicine.description, maxLines = 1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.Group, contentDescription = null)
                    Text("${donationRequest.contributorsCount} Contributors")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.Timer, contentDescription = null)
                    val duration =
                        (donationRequest.endDate - System.currentTimeMillis()).toDuration(
                            DurationUnit.DAYS
                        )
                    Text("ends in ${Random.nextInt(1, 100)} days")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonationListItem(
    donationRequest: DonationRequest,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    onDonateClick: () -> Unit = { }
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(id = R.drawable.donation_box),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(donationRequest.medicine.name, fontWeight = FontWeight.Bold)
            Text(donationRequest.medicine.description, maxLines = 2)
            val progress = donationRequest.collected.toFloat() / donationRequest.needed.toFloat()
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "${donationRequest.collected} / ${donationRequest.needed} units collected",
                        fontSize = 12.sp
                    )
                    Text(
                        "ends in ${Random.nextInt(1, 100)} days",
                        fontSize = 12.sp
                    )
                }

                Button(onClick = onDonateClick) {
                    Text("Donate")
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
                donationRequests = dummyDonationRequests(),
                query = "paracetamol"
            )
        )
    }

}