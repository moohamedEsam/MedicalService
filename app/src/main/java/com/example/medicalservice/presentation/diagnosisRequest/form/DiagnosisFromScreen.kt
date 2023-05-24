package com.example.medicalservice.presentation.diagnosisRequest.form

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composecomponents.OneTimeEventButton
import com.example.composecomponents.textField.OutlinedSearchTextField
import com.example.model.app.disease.Symptom
import com.example.model.app.disease.dummyList
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiagnosisFormScreen(
    viewModel: DiagnosisFormViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DiagnosisRequestFormScreen(
        state = uiState,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun DiagnosisRequestFormScreen(
    state: DiagnosisFormState,
    onEvent: (DiagnosisFormEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SymptomsList(state, onEvent)
        DescriptionTextField(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth()
        )
        OneTimeEventButton(
            enabled = state.isDiagnosisButtonEnabled,
            loading = state.isLoading,
            label = "Submit",
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(DiagnosisFormEvent.OnSubmitClick) }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SymptomsList(
    state: DiagnosisFormState,
    onEvent: (DiagnosisFormEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text("Select Symptoms", style = MaterialTheme.typography.headlineSmall)
    }
    OutlinedSearchTextField(
        query = state.query,
        onQueryChange = { onEvent(DiagnosisFormEvent.OnQueryChange(it)) },
        modifier = Modifier.fillMaxWidth()
    )
    LazyColumn(
        modifier = Modifier
            .heightIn(max = (LocalConfiguration.current.screenHeightDp / 4).dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(state.symptoms.sortedBy { it.name }) { symptom ->
            Text(
                text = symptom.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface,
                        MaterialTheme.shapes.extraSmall
                    )
                    .padding(8.dp)
                    .clickable { onEvent(DiagnosisFormEvent.OnSymptomClick(symptom)) },
                textAlign = TextAlign.Center,
                color = if (state.selectedSymptoms.contains(symptom)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )


        }
    }
    if (state.selectedSymptoms.isNotEmpty())
        Text("Selected Symptoms", style = MaterialTheme.typography.headlineSmall)
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.selectedSymptoms) { symptom ->
            FilterChip(
                selected = true,
                onClick = { onEvent(DiagnosisFormEvent.OnSymptomClick(symptom)) },
                label = { Text(symptom.name) },
                leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DescriptionTextField(
    state: DiagnosisFormState,
    onEvent: (DiagnosisFormEvent) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        value = state.description,
        onValueChange = { onEvent(DiagnosisFormEvent.OnDescriptionChange(it)) },
        modifier = modifier.heightIn(min = (LocalConfiguration.current.screenHeightDp / 3).dp),
        label = { Text("Description") },
        placeholder = { Text("Enter long description of how you feel") },
    )
}

@Preview(showBackground = true)
@Composable
private fun DiagnosisFromScreenPreview() {
    Surface {
        DiagnosisRequestFormScreen(
            state = DiagnosisFormState(symptoms = Symptom.dummyList()),
            onEvent = {}
        )
    }

}