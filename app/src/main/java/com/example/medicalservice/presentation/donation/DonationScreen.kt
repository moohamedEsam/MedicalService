package com.example.medicalservice.presentation.donation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composecomponents.OneTimeEventButton
import com.example.composecomponents.textField.ValidationOutlinedTextField
import com.example.model.app.donation.dummyDonationRequests
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DonationScreen(
    donationRequestId: String,
    viewModel: DonationViewModel = koinViewModel { parametersOf(donationRequestId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DonationScreen(
        state = uiState,
        modifier = Modifier,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
private fun DonationScreen(
    state: DonationScreenState,
    modifier: Modifier = Modifier,
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
            onEvent = onEvent,
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
    onEvent: (DonationScreenEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        DonationHeader(
            state = state,
            onMedicineReadMoreClick = { onEvent(DonationScreenEvent.OnMedicineReadMoreClick) }
        )
    }
}

@Composable
private fun DonationHeader(
    state: DonationScreenState,
    onMedicineReadMoreClick: (String) -> Unit = {}
) {
    Text(text = "Donation Request", style = MaterialTheme.typography.headlineMedium)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state.donationRequest.medicine.name,
            style = MaterialTheme.typography.headlineSmall
        )
        TextButton(onClick = { onMedicineReadMoreClick(state.donationRequest.medicine.id) }) {
            Text(text = "Read more", style = MaterialTheme.typography.bodyMedium)
        }
    }
    Text(
        text = state.donationRequest.medicine.uses.firstOrNull() ?: "",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2
    )
    if (!state.progress.isNaN())
        LinearProgressIndicator(
            progress = state.progress,
            modifier = Modifier
                .fillMaxWidth()
                .scale(1f, 2f)
                .padding(top = 8.dp),
            strokeCap = StrokeCap.Round,
        )

    Text(
        "Quantity needed: ${state.donationRequest.needed - state.donationRequest.collected}",
        style = MaterialTheme.typography.bodyLarge
    )

}

@Composable
private fun ColumnScope.DonationBody(
    state: DonationScreenState,
    onEvent: (DonationScreenEvent) -> Unit
) {
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
        val donationRequests = dummyDonationRequests()
        DonationScreen(
            state = DonationScreenState(
                donationRequest = donationRequests.random(),
            ),
            onEvent = {}
        )
    }

}