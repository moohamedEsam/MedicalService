package com.example.medicalservice.presentation.diseasePrediction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.einvoicecomponents.OneTimeEventButton
import com.example.einvoicecomponents.textField.OutlinedSearchTextField
import com.example.models.DiseaseView
import com.example.models.Symptom
import com.example.models.dummyList
import com.example.models.headache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiseasePredictionScreen(
    onDiseaseClick: (String) -> Unit,
    onMedicineClick: (String) -> Unit,
    viewModel: DiseasePredictionViewModel = koinViewModel()
) {
    DiseasePredictionScreenContent(
        symptoms = viewModel.symptoms,
        query = viewModel.query,
        onQueryChange = viewModel::onQueryChanged,
        selectedSymptoms = viewModel.selectedSymptoms,
        onSymptomSelected = viewModel::onSymptomSelected,
        diseases = viewModel.diseases,
        isPredictButtonEnabled = viewModel.isPredictButtonEnabled,
        onPredictClick = viewModel::onPredictClick,
        isLoading = viewModel.isLoading,
        onDiseaseClick = onDiseaseClick,
        onMedicineClick = onMedicineClick
    )
}

@Composable
private fun DiseasePredictionScreenContent(
    symptoms: StateFlow<List<Symptom>>,
    query: StateFlow<String>,
    onQueryChange: (String) -> Unit,
    selectedSymptoms: StateFlow<Set<Symptom>>,
    onSymptomSelected: (Symptom) -> Unit,
    diseases: StateFlow<List<DiseaseView>>,
    isPredictButtonEnabled: StateFlow<Boolean>,
    onPredictClick: () -> Unit,
    isLoading: StateFlow<Boolean>,
    onDiseaseClick: (String) -> Unit,
    onMedicineClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchBar(queryState = query, onQueryChange = onQueryChange)
        SymptomsList(
            symptomsState = symptoms,
            onSymptomSelected = onSymptomSelected,
            selectedSymptoms = selectedSymptoms
        )

        SelectedSymptomsList(
            selectedSymptomsState = selectedSymptoms,
            onSymptomRemoved = onSymptomSelected
        )

        OneTimeEventButton(
            enabled = isPredictButtonEnabled,
            loading = isLoading,
            label = "Predict",
            onClick = onPredictClick,
            modifier = Modifier.align(Alignment.End)
        )
        PredictedDiseasesList(
            diseasesState = diseases,
            modifier = Modifier.weight(1f),
            onMedicineClick = onMedicineClick,
            onDiseaseClick = onDiseaseClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SymptomsList(
    symptomsState: StateFlow<List<Symptom>>,
    onSymptomSelected: (Symptom) -> Unit,
    selectedSymptoms: StateFlow<Set<Symptom>>
) {
    val symptoms by symptomsState.collectAsState()
    val selectedSymptoms by selectedSymptoms.collectAsState()
    val sortedSymptoms by remember {
        derivedStateOf {
            symptoms.sortedBy { it.name }
        }
    }
    Text(text = "Symptoms", style = MaterialTheme.typography.headlineMedium)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(sortedSymptoms) { symptom ->
            FilterChip(
                onClick = { onSymptomSelected(symptom) },
                label = { Text(symptom.name) },
                selected = selectedSymptoms.contains(symptom)
            )
        }
    }
}

@Composable
private fun SearchBar(
    queryState: StateFlow<String>,
    onQueryChange: (String) -> Unit
) {
    OutlinedSearchTextField(
        queryState = queryState,
        onQueryChange = onQueryChange,
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectedSymptomsList(
    selectedSymptomsState: StateFlow<Set<Symptom>>,
    onSymptomRemoved: (Symptom) -> Unit
) {
    val selectedSymptoms by selectedSymptomsState.collectAsState()
    Text(text = "Selected Symptoms", style = MaterialTheme.typography.headlineMedium)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(selectedSymptoms.toList().reversed()) { symptom ->
            SuggestionChip(
                onClick = { onSymptomRemoved(symptom) },
                label = { Text(symptom.name) },
            )
        }
    }
}

@Composable
private fun PredictedDiseasesList(
    diseasesState: StateFlow<List<DiseaseView>>,
    onDiseaseClick: (String) -> Unit,
    onMedicineClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val diseases by diseasesState.collectAsState()
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(diseases) { disease ->
            DiseaseItem(
                disease = disease,
                onDiseaseClick = onDiseaseClick,
                modifier = Modifier.heightIn(max = 300.dp),
                onMedicineClick = onMedicineClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiseaseItem(
    disease: DiseaseView,
    onDiseaseClick: (String) -> Unit = {},
    onMedicineClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { onDiseaseClick(disease.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(text = disease.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = "94 %", style = MaterialTheme.typography.bodyLarge)
            }
            Text(text = "Recommended Medicines", style = MaterialTheme.typography.bodyLarge)
            LazyColumn {
                items(disease.medicines) { medicine ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clickable { onMedicineClick(medicine.id) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = medicine.name, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Available")
                    }
                    Divider()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DiseasePredictionScreenPreview() {
    DiseasePredictionScreenContent(
        symptoms = MutableStateFlow(Symptom.dummyList()),
        query = MutableStateFlow(""),
        onQueryChange = {},
        selectedSymptoms = MutableStateFlow(Symptom.dummyList().shuffled().subList(0, 4).toSet()),
        onSymptomSelected = {},
        diseases = MutableStateFlow(listOf(DiseaseView.headache())),
        isPredictButtonEnabled = MutableStateFlow(false),
        onPredictClick = {},
        isLoading = MutableStateFlow(false),
        onDiseaseClick = {},
        onMedicineClick = {}
    )
}