package com.example.medicalservice.presentation.medicine

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.models.*
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.random.Random

@Composable
fun MedicineDetailsScreen(
    medicineId: String,
    onDiseaseClick: (String) -> Unit,
    viewModel: MedicineDetailsViewModel = koinViewModel { parametersOf(medicineId) }
) {
    val medicine by viewModel.medicine.collectAsState()
    MedicineDetailsScreenContent(
        medicine = medicine,
        onDiseaseClick = onDiseaseClick
    )
}

@Composable
private fun MedicineDetailsScreenContent(
    medicine: MedicineView,
    onDiseaseClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MedicineName(medicine)
        MedicineDescription(medicine)
        MedicineDiseases(medicine, onDiseaseClick)
        MedicineSideEffects(medicine)
        MedicinePreCautions(medicine)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MedicinePreCautions(medicine: MedicineView) {
    Text(text = "Precautions", style = MaterialTheme.typography.headlineMedium)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(medicine.precautions) { precaution ->
            SuggestionChip(onClick = { }, label = { Text(text = precaution) })
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MedicineSideEffects(medicine: MedicineView) {
    Text(text = "Side Effects", style = MaterialTheme.typography.headlineMedium)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(medicine.sideEffects) { sideEffect ->
            SuggestionChip(onClick = { }, label = { Text(text = sideEffect) })
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MedicineDiseases(
    medicine: MedicineView,
    onDiseaseClick: (String) -> Unit
) {
    Text(text = "For Diseases", style = MaterialTheme.typography.headlineMedium)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(medicine.diseases) { disease ->
            SuggestionChip(
                onClick = { onDiseaseClick(disease.id) },
                label = { Text(text = disease.name) })
        }
    }
}

@Composable
private fun MedicineDescription(medicine: MedicineView) {
    Text(text = "Description", style = MaterialTheme.typography.headlineMedium)
    Text(text = medicine.description, style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun MedicineName(medicine: MedicineView) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = medicine.name, style = MaterialTheme.typography.headlineLarge)
        if (Random.nextBoolean())
            Text(
                text = "Out Of Stock",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        else
            Text(
                text = "Available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
    }
}


@Preview(showBackground = true)
@Composable
private fun MedicineDetailsScreenPreview() {
    MedicineDetailsScreenContent(
        medicine = MedicineView.paracetamol()
    )
}