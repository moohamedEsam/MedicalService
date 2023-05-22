package com.example.medicalservice.presentation.home.receiver

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.example.medicalservice.R
import com.example.medicalservice.presentation.components.VerticalTransactionsList
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.diagnosis.empty
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import com.example.model.app.transaction.TransactionView
import com.example.model.app.transaction.empty
import com.example.model.app.user.User
import com.example.model.app.user.emptyDoctor
import com.example.model.app.user.emptyReceiver
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReceiverHomeScreen(
    viewModel: ReceiverHomeViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ReceiverHomeScreen(
        state = state,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun ReceiverHomeScreen(
    state: ReceiverHomeScreenState,
    onEvent: (ReceiverHomeScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReceiverHomeScreenHeader(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.align(Alignment.End)
        )
        ReceiverHomeScreenTransactions(
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
private fun ReceiverHomeScreenHeader(
    state: ReceiverHomeScreenState,
    onEvent: (ReceiverHomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hello, ${state.user.username}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.faq),
                contentDescription = null,
                modifier = Modifier.scale(0.75f)
            )
        }

        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.feed_back),
                contentDescription = null,
                modifier = Modifier.scale(0.75f)
            )
        }

    }
    if (state.latestDiagnosisResult == null)
        UploadDiagnosisRequest(onEvent = onEvent)
    else
        LatestDiagnosisResult(
            diagnosisResultView = state.latestDiagnosisResult,
            onEvent = onEvent
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UploadDiagnosisRequest(
    onEvent: (ReceiverHomeScreenEvent) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        onClick = { onEvent(ReceiverHomeScreenEvent.OnCreateDiagnosisRequestClicked) }
    ) {
        Spacer(modifier = Modifier.weight(0.4f))
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(0.6f)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "You have no diagnosis requests yet",
                style = MaterialTheme.typography.bodyLarge,
            )

            TextButton(onClick = { onEvent(ReceiverHomeScreenEvent.OnCreateDiagnosisRequestClicked) }) {
                Text(text = "Upload diagnosis")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun LatestDiagnosisResult(
    diagnosisResultView: DiagnosisResultView,
    onEvent: (ReceiverHomeScreenEvent) -> Unit,
    dateFormatter: SimpleDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onEvent(ReceiverHomeScreenEvent.OnDiagnosisClicked) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Latest diagnosis result",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = diagnosisResultView.diagnosis,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "diagnosed: ${dateFormatter.format(diagnosisResultView.createdAt)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "last updated: ${dateFormatter.format(diagnosisResultView.updatedAt)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Doctor: ")
                    TextButton(onClick = { }) {
                        Text(text = diagnosisResultView.doctor.username)
                    }
                }

                Text(
                    text = "Status: ${diagnosisResultView.status}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ReceiverHomeScreenTransactions(
    state: ReceiverHomeScreenState,
    onEvent: (ReceiverHomeScreenEvent) -> Unit,
) {
    VerticalTransactionsList(
        transactionViewsFlow = state.transactions,
        onTransactionClick = { onEvent(ReceiverHomeScreenEvent.OnTransactionClicked(it.id)) },
        onMedicineClick = { onEvent(ReceiverHomeScreenEvent.OnMedicineClicked(it)) },
        title = "Recent Transactions"
    )
}


@Preview(showBackground = true)
@Composable
private fun ReceiverHomeScreenPreview() {
    Surface {
        ReceiverHomeScreen(
            state = ReceiverHomeScreenState(
                transactions = flowOf(
                    PagingData.from(
                        listOf(
                            TransactionView.empty().copy(medicine = MedicineView.paracetamol())
                        )
                    )
                ),
                latestDiagnosisResult = DiagnosisResultView.empty().copy(
                    diagnosis = "Based on your symptoms, it sounds like you have a viral infection. This is a common cause of fever. The good news is that most viral infections go away on their own within a week or two.",
                    doctor = User.emptyDoctor().copy(username = "Dr. John Doe")
                ),
                user = User.emptyReceiver().copy(username = "mohamed"),
                isSearchVisible = true,
            ),
            onEvent = {}
        )
    }
}
