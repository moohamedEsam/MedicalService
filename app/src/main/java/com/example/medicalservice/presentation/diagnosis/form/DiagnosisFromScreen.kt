package com.example.medicalservice.presentation.diagnosis.form

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
        AttachFileButton(onEvent)
    }
    OutlinedSearchTextField(
        query = state.query,
        onQueryChange = { onEvent(DiagnosisFormEvent.OnQueryChange(it)) },
        modifier = Modifier.fillMaxWidth()
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.symptoms.sortedBy { it.name }) { symptom ->
            FilterChip(
                selected = state.selectedSymptoms.contains(symptom),
                onClick = { onEvent(DiagnosisFormEvent.OnSymptomClick(symptom)) },
                label = { Text(symptom.name) },
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

@Composable
private fun AttachFileButton(onEvent: (DiagnosisFormEvent) -> Unit) {
    var isFileAttached by remember {
        mutableStateOf(false)
    }
    val imageContract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            onEvent(DiagnosisFormEvent.OnImagePicked(it.toString()))
            isFileAttached = true
        }
    }
    Column(
        modifier = Modifier.animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                imageContract.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        ) {
            Icon(Icons.Default.AttachFile, contentDescription = null)
        }
        if (isFileAttached)
            Text("File Attached")
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