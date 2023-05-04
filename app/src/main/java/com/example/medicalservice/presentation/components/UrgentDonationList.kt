package com.example.medicalservice.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medicalservice.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun UrgentDonationList(
    donationRequestViews: List<com.example.model.app.DonationRequestView>,
    title: String,
    isDonateButtonVisible: Boolean = true,
    onDonationRequestCardClick: (com.example.model.app.DonationRequestView) -> Unit = {},
    onDonationRequestClick: (com.example.model.app.DonationRequestView) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val visibleItemIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    LaunchedEffect(key1 = visibleItemIndex) {
        delay(3000)
        if (visibleItemIndex < donationRequestViews.size - 1)
            listState.animateScrollToItem(visibleItemIndex + 1)
        else
            listState.scrollToItem(0)

    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        TextButton(onClick = { }) {
            Text("See All")
        }
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
    ) {
        items(donationRequestViews, key = { it.id }) {
            DonationListItem(
                donationRequestView = it,
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.9f),
                isDonateButtonVisible = isDonateButtonVisible,
                onDonateClick = { onDonationRequestClick(it) },
                onClick = { onDonationRequestCardClick(it) }
            )
        }
        item { Spacer(modifier = Modifier.width(16.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonationListItem(
    donationRequestView: com.example.model.app.DonationRequestView,
    modifier: Modifier = Modifier,
    isDonateButtonVisible: Boolean = true,
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
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(donationRequestView.medicine.name, fontWeight = FontWeight.Bold)
            Text(donationRequestView.medicine.description, maxLines = 2)
            val progress = donationRequestView.collected.toFloat() / donationRequestView.needed.toFloat()
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
                        "${donationRequestView.collected} / ${donationRequestView.needed} units collected",
                        fontSize = 12.sp
                    )
                    Text(
                        "ends in ${Random.nextInt(1, 100)} days",
                        fontSize = 12.sp
                    )
                }
                if (isDonateButtonVisible)
                    Button(onClick = onDonateClick) {
                        Text("Donate")
                    }
            }
        }
    }
}