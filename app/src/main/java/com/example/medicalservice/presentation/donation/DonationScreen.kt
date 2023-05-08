package com.example.medicalservice.presentation.donation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.example.composecomponents.OneTimeEventButton
import com.example.composecomponents.textField.OutlinedSearchTextField
import com.example.composecomponents.textField.ValidationOutlinedTextField
import com.example.medicalservice.presentation.components.UrgentDonationList
import com.example.model.app.DonationRequestView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DonationScreen(
    donationRequestId: String,
    onMedicineReadMoreClick: (String) -> Unit,
    viewModel: DonationViewModel = koinViewModel { parametersOf(donationRequestId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DonationScreen(
        state = uiState,
        modifier = Modifier,
        onEvent = viewModel::handleEvent,
        onMedicineReadMoreClick = onMedicineReadMoreClick
    )
}

@Composable
private fun DonationScreen(
    state: DonationScreenState,
    modifier: Modifier = Modifier,
    onMedicineReadMoreClick: (String) -> Unit = {},
    onEvent: (DonationScreenEvent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DonationHeader(
            state = state,
            onDonationRequestClick = { onEvent(DonationScreenEvent.OnDonationRequestSelected(it)) },
            onQueryChange = { onEvent(DonationScreenEvent.OnQueryChange(it)) },
            onChooseAnotherDonationRequest = { onEvent(DonationScreenEvent.OnChooseAnotherDonationRequest) },
            onMedicineReadMoreClick = onMedicineReadMoreClick
        )
        DonationBody(
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
private fun DonationHeader(
    state: DonationScreenState,
    onDonationRequestClick: (DonationRequestView) -> Unit,
    onQueryChange: (String) -> Unit,
    onChooseAnotherDonationRequest: () -> Unit,
    onMedicineReadMoreClick: (String) -> Unit = {},
) {
    AnimatedVisibility(
        visible = state.selectedDonationRequest == null,
        enter = fadeIn()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DonationHeader(
                query = state.query,
                donationRequestViews = state.donationRequestViews,
                onDonationRequestClick = onDonationRequestClick,
                onQueryChange = onQueryChange
            )
        }
    }

    AnimatedVisibility(
        visible = state.selectedDonationRequest != null,
        enter = fadeIn()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            DonationHeader(
                donationRequestView = state.selectedDonationRequest,
                onChooseAnotherDonationRequest = onChooseAnotherDonationRequest,
                onMedicineReadMoreClick = onMedicineReadMoreClick
            )
        }
    }
}

@Composable
private fun DonationHeader(
    query: String,
    donationRequestViews: Flow<PagingData<DonationRequestView>>,
    onDonationRequestClick: (DonationRequestView) -> Unit,
    onQueryChange: (String) -> Unit,
) {
    OutlinedSearchTextField(
        query = query,
        onQueryChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        label = "Search for donation requests",
    )

    UrgentDonationList(
        donationRequestViewPagingData = donationRequestViews,
        title = "Donations Requests",
        isDonateButtonVisible = false,
        onDonationRequestCardClick = { onDonationRequestClick(it) }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DonationHeader(
    donationRequestView: DonationRequestView?,
    onChooseAnotherDonationRequest: () -> Unit = {},
    onMedicineReadMoreClick: (String) -> Unit = {}
) {
    if (donationRequestView == null) return
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Donation Request", style = MaterialTheme.typography.headlineMedium)
        TextButton(onClick = onChooseAnotherDonationRequest) {
            Text(text = "Choose Another Donation", style = MaterialTheme.typography.bodyMedium)
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = donationRequestView.medicine.name, style = MaterialTheme.typography.headlineSmall)
        TextButton(onClick = { onMedicineReadMoreClick(donationRequestView.medicine.id) }) {
            Text(text = "Read more", style = MaterialTheme.typography.bodyMedium)
        }
    }
    Text(
        text = donationRequestView.medicine.uses.firstOrNull()?:"",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2
    )
    val progress = donationRequestView.collected.toFloat() / donationRequestView.needed.toFloat()
    LinearProgressIndicator(
        progress = progress,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
    Text(
        "Quantity needed: ${donationRequestView.needed - donationRequestView.collected}",
        style = MaterialTheme.typography.bodyLarge
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Outlined.Timer, contentDescription = null)
        Text(text = "ends in 4 days", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ColumnScope.DonationBody(
    state: DonationScreenState,
    onEvent: (DonationScreenEvent) -> Unit
) {
    if (state.selectedDonationRequest == null) return
    ValidationOutlinedTextField(
        value = state.quantity,
        validation = state.quantityValidationResult,
        label = "Quantity",
        onValueChange = {
            onEvent(DonationScreenEvent.OnQuantityChange(it))
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
    OneTimeEventButton(
        enabled = state.isDonateButtonEnabled,
        loading = state.isLoading,
        label = "Donate",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        onEvent(DonationScreenEvent.OnDonateClick)
    }
}

@Preview(showBackground = true)
@Composable
private fun DonationScreenPreview() {
    Surface {
        val donationRequests = donationRequests()
        DonationScreen(
            state = DonationScreenState(
                donationRequestViews = flowOf(PagingData.from(donationRequests)),
                selectedDonationRequest = donationRequests.random(),
            ),
            onEvent = {}
        )
    }

}

@Composable
private fun donationRequests() = com.example.model.app.dummyDonationRequests()