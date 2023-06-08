package com.example.medicalservice.presentation.medicine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.*
import com.example.model.app.medicine.MedicineView
import com.example.model.app.medicine.paracetamol
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MedicineDetailsScreen(
    medicineId: String,
    viewModel: MedicineDetailsViewModel = koinViewModel { parametersOf(medicineId) }
) {
    val medicine by viewModel.medicine.collectAsState()
    MedicineDetailsScreenContent(
        medicine = medicine,
        onDiseaseClick = viewModel::onDiseaseClick
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
private fun MedicinePreCautions(medicine: MedicineView) {
    Text(text = "Precautions", style = MaterialTheme.typography.headlineMedium)
    Text(text = medicine.precautions.joinToString("\n"), style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun MedicineSideEffects(medicine: MedicineView) {
    Text(text = "Side Effects", style = MaterialTheme.typography.headlineMedium)
    Text(text = medicine.sideEffects.joinToString("\n"), style = MaterialTheme.typography.bodyLarge)
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
    Text(text = medicine.uses.joinToString("\n"), style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun MedicineName(medicine: MedicineView) {
    Text(text = medicine.name, style = MaterialTheme.typography.headlineLarge)
}


@Preview(showBackground = true)
@Composable
private fun MedicineDetailsScreenPreview() {
    MedicineDetailsScreenContent(
        medicine = MedicineView.paracetamol()
    )
}