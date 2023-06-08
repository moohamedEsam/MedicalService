package com.example.medicalservice.presentation.diagnosisResult.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicalservice.presentation.components.ExpandableColumn
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.DiagnosisResult
import com.example.model.app.diagnosis.DiagnosisResultView
import com.example.model.app.diagnosis.empty
import com.example.model.app.disease.Symptom
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import com.example.model.app.user.User
import com.example.model.app.user.emptyDoctor
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DiagnosisDetailsScreen(
    diagnosisId: String,
    viewModel: DiagnosisDetailsViewModel = koinViewModel { parametersOf(diagnosisId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DiagnosisDetailsScreen(uiState, viewModel::handleEvent)
}

@Composable
private fun DiagnosisDetailsScreen(
    state: DiagnosisDetailsScreenState,
    onEvent: (DiagnosisDetailsScreenEvent) -> Unit,
    dateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault()),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DiagnosisMainDetails(state, onEvent, dateFormat)
        DiagnosisMedicationsList(
            state = state,
            modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp / 4).dp),
            onEvent = onEvent
        )

        ExpandableColumn(
            title = {
                Text(
                    text = "Diagnosis Request",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        ) {
            DiagnosisRequestDetails(state)
        }
    }
}

@Composable
private fun DiagnosisMedicationsList(
    state: DiagnosisDetailsScreenState,
    modifier: Modifier,
    onEvent: (DiagnosisDetailsScreenEvent) -> Unit
) {
    Text("Medications", style = MaterialTheme.typography.headlineSmall)
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(state.diagnosisResultView.medications) { medication ->
            MedicineItem(
                medicineView = medication,
                onClick = { onEvent(DiagnosisDetailsScreenEvent.OnMedicineClick(medication.id)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun DiagnosisMainDetails(
    state: DiagnosisDetailsScreenState,
    onEvent: (DiagnosisDetailsScreenEvent) -> Unit,
    dateFormat: SimpleDateFormat
) {
    Text(text = "Diagnosis Details", style = MaterialTheme.typography.headlineMedium)
    Text(text = state.diagnosisResultView.diagnosis, style = MaterialTheme.typography.bodyLarge)
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Doctor: ${state.diagnosisResultView.doctor?.username}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { onEvent(DiagnosisDetailsScreenEvent.OnDoctorClick) }
        )

        Text(
            text = "Status: ${state.diagnosisResultView.status}",
            style = MaterialTheme.typography.bodyMedium
        )
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Created: ${dateFormat.format(state.diagnosisResultView.createdAt)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Updated: ${dateFormat.format(state.diagnosisResultView.updatedAt)}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineItem(
    medicineView: Medicine,
    onClick: (Medicine) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier, onClick = { onClick(medicineView) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = medicineView.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = medicineView.uses.firstOrNull() ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun DiagnosisRequestDetails(
    state: DiagnosisDetailsScreenState
) {
    val diagnosisRequest = state.diagnosisResultView.request

    Text(text = "Symptoms", style = MaterialTheme.typography.headlineSmall)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(diagnosisRequest.symptoms) { symptom ->
            SuggestionChip(onClick = { }, label = { Text(text = symptom.name) })
        }
    }
    Text(text = "Description", style = MaterialTheme.typography.headlineSmall)
    Text(
        text = diagnosisRequest.description,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
private fun DiagnosisDetailsScreenPreview() {
    val diagnosis =
        "Acute sinusitis is an inflammation of the sinuses, which are air-filled cavities located in the bones of the skull. The sinuses are lined with a thin layer of mucus membrane, which helps to trap dust, pollen, and other particles from the air. When this mucus membrane becomes inflamed, it can produce more mucus, which can block the sinus openings and cause pain, pressure, and swelling."
    val doctor: User.Doctor = User.emptyDoctor().copy(username = "Dr. Smith")
    val status: DiagnosisResult.Status = DiagnosisResult.Status.Pending
    val id = "1234567890"
    val createdAt = Date()
    val updatedAt = Date()
    val diagnosisResultView = DiagnosisResultView(
        diagnosis = diagnosis,
        doctor = doctor,
        status = status,
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        request = DiagnosisRequest.empty().copy(
            symptoms = listOf(
                Symptom("Headache"),
                Symptom("Fever"),
                Symptom("Nasal congestion"),
                Symptom("Facial pain")
            ),
        ),
        medications = listOf(
            Medicine.empty().copy(name = "Nasal decongestant"),
            Medicine.empty().copy(name = "Pain reliever"),
            Medicine.empty().copy(name = "Antihistamine"),
        ),
        disease = null
    )

    Surface {
        DiagnosisDetailsScreen(
            state = DiagnosisDetailsScreenState(diagnosisResultView = diagnosisResultView),
            onEvent = {},
        )
    }

}