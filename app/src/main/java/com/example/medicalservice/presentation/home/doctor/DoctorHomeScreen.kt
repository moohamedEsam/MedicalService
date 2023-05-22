package com.example.medicalservice.presentation.home.doctor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.composecomponents.loadStateItem
import com.example.model.app.diagnosis.DiagnosisResultView
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DoctorHomeScreen(
    viewModel: DoctorHomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DoctorHomeScreen(
        state = uiState,
        onEventSent = viewModel::handleEvent
    )
}

@Composable
private fun DoctorHomeScreen(
    state: DoctorHomeScreenState,
    onEventSent: (DoctorHomeScreenEvent) -> Unit
) {
    val diagnosisResults = state.diagnosisResults.collectAsLazyPagingItems()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if(diagnosisResults.itemCount == 0)
            item {
                Text(
                    text = "No diagnosis requests yet",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        items(diagnosisResults) {
            if (it == null) return@items
            DiagnosisResultItem(
                diagnosisResult = it,
                onEventSent = onEventSent,
                modifier = Modifier.fillMaxWidth()
            )
        }
        loadStateItem(diagnosisResults.loadState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiagnosisResultItem(
    diagnosisResult: DiagnosisResultView,
    onEventSent: (DoctorHomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
    dateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
) {
    Card(
        modifier = modifier,
        onClick = { onEventSent(DoctorHomeScreenEvent.DiagnosisResultClicked(diagnosisResult.id)) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = diagnosisResult.request.description, maxLines = 3)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(diagnosisResult.request.symptoms) { symptom ->
                    AssistChip(onClick = { }, label = { Text(text = symptom.name) })
                }
            }
            Text(text = "Created at: ${dateFormat.format(diagnosisResult.createdAt)}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DoctorHomeScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        DoctorHomeScreen(
            state = DoctorHomeScreenState(
//                diagnosisResults = flowOf(
//                    PagingData.from(listOf(DiagnosisResultView.empty()))
//                )
            ),
            onEventSent = {}
        )
    }

}