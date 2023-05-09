package com.example.medicalservice.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.medicalservice.R
import com.example.model.app.DonationRequestView
import com.example.model.app.dummyDonationRequests
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

@Composable
fun UrgentDonationList(
    donationRequestViewPagingData: Flow<PagingData<DonationRequestView>>,
    title: String,
    isDonateButtonVisible: Boolean = true,
    onDonationRequestCardClick: (DonationRequestView) -> Unit = {},
    onDonationRequestClick: (DonationRequestView) -> Unit = {},
    onBookmarkClick: (DonationRequestView) -> Unit = {},
    onSeeAllClick: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    val donationRequestViews = donationRequestViewPagingData.collectAsLazyPagingItems()
    val visibleItemIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    LaunchedEffect(key1 = visibleItemIndex) {
        delay(3000)
        if (visibleItemIndex < donationRequestViews.itemCount - 1)
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
        TextButton(onClick = onSeeAllClick) {
            Text("See All")
        }
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
    ) {
        items(donationRequestViews, key = { it.id }) {
            if (it == null) return@items
            DonationListItem(
                donationRequestView = it,
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.9f),
                isDonateButtonVisible = isDonateButtonVisible,
                onDonateClick = { onDonationRequestClick(it) },
                onClick = { onDonationRequestCardClick(it) },
                onBookmarkClick = { onBookmarkClick(it) }
            )
        }
        item { Spacer(modifier = Modifier.width(16.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonationListItem(
    donationRequestView: DonationRequestView,
    modifier: Modifier = Modifier,
    isDonateButtonVisible: Boolean = true,
    onClick: () -> Unit = { },
    onDonateClick: () -> Unit = { },
    onBookmarkClick: () -> Unit = { }
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.donation_box),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
            )
            IconButton(
                onClick = onBookmarkClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                if (donationRequestView.isBookmarked)
                    Icon(imageVector = Icons.Outlined.Bookmark, contentDescription = null)
                else
                    Icon(imageVector = Icons.Outlined.BookmarkBorder, contentDescription = null)
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(donationRequestView.medicine.name, fontWeight = FontWeight.Bold)
            Text(donationRequestView.medicine.uses.firstOrNull() ?: "", maxLines = 2)
            val progress =
                donationRequestView.collected.toFloat() / donationRequestView.needed.toFloat()
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

@Preview(showBackground = true)
@Composable
private fun UrgentDonationListPreview() {
    Surface {
        UrgentDonationList(
            donationRequestViewPagingData = flowOf(
                PagingData.from(
                    dummyDonationRequests()
                )
            ),
            title = "Urgent Donations"
        )
    }

}