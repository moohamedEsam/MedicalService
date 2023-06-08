package com.example.medicalservice.presentation.diagnosisResult.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.empty
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.Symptom
import com.example.model.app.disease.dummyList
import com.example.model.app.disease.headache
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.empty
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun DiagnosisResultFormScreen(
    diagnosisResultId: String,
    viewModel: DiagnosisResultFormViewModel = koinViewModel { parametersOf(diagnosisResultId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DiagnosisResultFormScreen(state = uiState, onEvent = viewModel::handleEvent)
}

@Composable
private fun DiagnosisResultFormScreen(
    state: DiagnosisResultFormState,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DiagnosisRequest(request = state.diagnosisRequest)
        DiagnosisResult(state = state, onEvent = onEvent)
        Button(
            onClick = { onEvent(DiagnosisResultFormEvent.Form.OnSaveClick) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraSmall,
            enabled = state.isSaveButtonEnabled
        ) {
            Text(text = "Save")
        }
    }
    if (state.isUnregisteredDiseaseDialogVisible)
        UnregisteredDiseaseDialog(state, onEvent)
    if (state.isUnregisteredMedicineDialogVisible)
        UnregisteredMedicineDialog(state, onEvent)
}

@Composable
private fun UnregisteredMedicineDialog(
    state: DiagnosisResultFormState,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEvent(DiagnosisResultFormEvent.UnregisteredMedicineDialog.Dismiss) },
        confirmButton = {
            TextButton(onClick = { onEvent(DiagnosisResultFormEvent.UnregisteredMedicineDialog.OnSaveClick) }) {
                Text(text = "Add")
            }
        },
        text = {
            TextField(
                value = state.unregisteredMedicineValue,
                onValueChange = {
                    onEvent(
                        DiagnosisResultFormEvent.UnregisteredMedicineDialog.OnMedicineChange(
                            it
                        )
                    )
                },
                label = { Text(text = "Medicine name") }
            )
        },
    )
}

@Composable
private fun UnregisteredDiseaseDialog(
    state: DiagnosisResultFormState,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEvent(DiagnosisResultFormEvent.UnregisteredDiseaseDialog.Dismiss) },
        confirmButton = {
            TextButton(onClick = { onEvent(DiagnosisResultFormEvent.UnregisteredDiseaseDialog.OnSaveClick) }) {
                Text(text = "Add")
            }
        },
        text = {
            TextField(
                value = state.unregisteredDiseaseValue,
                onValueChange = {
                    onEvent(
                        DiagnosisResultFormEvent.UnregisteredDiseaseDialog.OnDiseaseChange(
                            it
                        )
                    )
                },
                label = { Text(text = "Disease name") }
            )
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DiagnosisRequest(
    request: DiagnosisRequest,
    dateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd yy", Locale.getDefault())
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Diagnosis Request", style = MaterialTheme.typography.headlineMedium)
        Text(text = dateFormat.format(request.date))
    }
    Text(text = request.description, style = MaterialTheme.typography.bodyLarge)
    Text(text = "Symptoms", style = MaterialTheme.typography.headlineSmall)
    if (request.symptoms.isEmpty())
        Text(text = "No symptoms added", style = MaterialTheme.typography.bodyLarge)
    else
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(request.symptoms) { symptom ->
                Text(
                    text = symptom.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
}

@Composable
private fun ColumnScope.DiagnosisResult(
    state: DiagnosisResultFormState,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    Text(text = "diagnosis", style = MaterialTheme.typography.headlineMedium)
    TextField(
        value = state.diagnosis,
        onValueChange = { onEvent(DiagnosisResultFormEvent.Form.OnDiagnosisChange(it)) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = (LocalConfiguration.current.screenHeightDp / 4).dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text(text = "diagnosis") },
    )
    DiagnosisDisease(state, onEvent)
    DiagnosisMedications(state, onEvent)

}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun DiagnosisDisease(
    state: DiagnosisResultFormState,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = "disease", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.weight(1f))
        if (!state.isAddDiseaseVisible) return@FlowRow
        TextButton(onClick = { onEvent(DiagnosisResultFormEvent.Form.OnAddUnRegisteredDiseaseClick) }) {
            Text("Add Unregistered Disease")
        }
        IconButton(onClick = { onEvent(DiagnosisResultFormEvent.Form.OnAddDiseaseClick) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }

    }

    DiseaseOptionsSearch(state, onEvent)
    if (state.disease == null) return
    OutlinedCard(
        onClick = { onEvent(DiagnosisResultFormEvent.Form.OnDiseaseClick) },
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = state.disease.name, style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = { onEvent(DiagnosisResultFormEvent.Form.OnRemoveDiseaseClick) }) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Text(
                text = state.disease.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 3
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DiseaseOptionsSearch(
    state: DiagnosisResultFormState,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxWidth(),
        visible = state.isDiseaseSearchBarVisible
    ) {
        DockedSearchBar(
            query = state.diseaseOptionsSearchQuery,
            onQueryChange = { onEvent(DiagnosisResultFormEvent.DiseaseOptionSearch.OnQueryChange(it)) },
            onSearch = { onEvent(DiagnosisResultFormEvent.DiseaseOptionSearch.OnQueryChange(it)) },
            active = true,
            onActiveChange = { if (!it) onEvent(DiagnosisResultFormEvent.DiseaseOptionSearch.Dismiss) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search Diseases") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            shape = MaterialTheme.shapes.small,
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.filteredDiseaseOptions) {
                    ListItem(
                        headlineContent = { Text(text = it.name) },
                        supportingContent = { Text(text = it.description, maxLines = 1) },
                        modifier = Modifier.clickable {
                            onEvent(DiagnosisResultFormEvent.DiseaseOptionSearch.OnDiseaseClick(it.id))
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineOptionsSearch(
    state: DiagnosisResultFormState,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxWidth(),
        visible = state.isMedicineOptionSearchVisible
    ) {
        DockedSearchBar(
            query = state.medicineOptionSearchQuery,
            onQueryChange = { onEvent(DiagnosisResultFormEvent.MedicineOptionSearch.OnQueryChange(it)) },
            onSearch = { onEvent(DiagnosisResultFormEvent.MedicineOptionSearch.OnQueryChange(it)) },
            active = true,
            onActiveChange = {if(!it) onEvent(DiagnosisResultFormEvent.MedicineOptionSearch.Dismiss) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search Medicines") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            shape = MaterialTheme.shapes.small,
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.filteredMedicineOptions) {
                    ListItem(
                        headlineContent = { Text(text = it.name) },
                        supportingContent = {
                            Text(
                                text = it.uses.firstOrNull() ?: "",
                                maxLines = 1
                            )
                        },
                        modifier = Modifier.clickable {
                            onEvent(DiagnosisResultFormEvent.MedicineOptionSearch.OnMedicineClick(it.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.DiagnosisMedications(
    state: DiagnosisResultFormState,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    Text(text = "medications", style = MaterialTheme.typography.headlineMedium)
    Row(
        modifier = Modifier.align(Alignment.End),
    ) {
        TextButton(onClick = { onEvent(DiagnosisResultFormEvent.Form.OnAddUnRegisteredMedicineClick) }) {
            Text("Add Unregistered Medicine")
        }
        IconButton(onClick = { onEvent(DiagnosisResultFormEvent.Form.OnAddMedicineClick) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
    MedicineOptionsSearch(state = state, onEvent = onEvent)
    if (state.medications.isEmpty())
        Text(text = "No medications added", style = MaterialTheme.typography.bodyLarge)
    else
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp / 4).dp)
        ) {
            items(state.medications) { medication ->
                MedicineItem(medication, onEvent)
            }
            items(state.unRegisteredMedicines) {
                UnRegisteredMedicineItem(medicineName = it, onEvent = onEvent)
            }
        }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MedicineItem(
    medication: Medicine,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    OutlinedCard(
        onClick = { onEvent(DiagnosisResultFormEvent.Form.OnMedicineClick(medication.id)) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(
                    onClick = { onEvent(DiagnosisResultFormEvent.Form.OnMedicineDelete(medication.id)) }
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Text(
                text = medication.uses.firstOrNull() ?: "",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun UnRegisteredMedicineItem(
    medicineName: String,
    onEvent: (DiagnosisResultFormEvent) -> Unit
) {
    OutlinedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = medicineName,
                style = MaterialTheme.typography.headlineSmall
            )

            IconButton(
                onClick = {
                    onEvent(
                        DiagnosisResultFormEvent.Form.OnUnRegisteredMedicineDelete(medicineName)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiagnosisResultFormPreview() {
    Surface {
        DiagnosisResultFormScreen(
            state = DiagnosisResultFormState(
                diagnosisRequest = DiagnosisRequest.empty().copy(
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    symptoms = Symptom.dummyList()
                ),
                disease = DiseaseView.headache()
                    .copy(medicines = listOf(Medicine.empty().copy("Ibuprofen"))),
                unRegisteredMedicines = listOf("Ibuprofen"),
                isDiseaseSearchBarVisible = true,
                diseaseOptions = listOf(DiseaseView.headache()),
                isMedicineOptionSearchVisible = true,
            ),
            onEvent = {}
        )
    }

}