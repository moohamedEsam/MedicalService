package com.example.medicalservice.presentation.disease

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.headache
import com.example.model.app.medicine.Medicine
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val SEPARATOR = "\n\n"

@Composable
fun DiseaseScreen(
    diseaseId: String,
    viewModel: DiseaseViewModel = koinViewModel { parametersOf(diseaseId) }
) {
    val disease by viewModel.disease.collectAsState()
    DiseaseScreenContent(
        disease = disease,
        onMedicineClick = viewModel::onMedicineClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DiseaseScreenContent(
    disease: DiseaseView,
    onMedicineClick: (String) -> Unit
) {
    val pages = listOf(
        "Description",
        "Symptoms",
        "Treatment",
        "Prevention",
        "Diagnosis",
        "Medicines"
    )
    val pagerState = rememberPagerState()
    var pageToScroll by remember {
        mutableStateOf(pagerState.currentPage)
    }
    LaunchedEffect(pageToScroll) {
        pagerState.animateScrollToPage(pageToScroll)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            pages.forEachIndexed { index, page ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { pageToScroll = index },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = page)
                }
            }
        }
        DiseaseContentPager(
            pages = pages,
            pagerState = pagerState,
            disease = disease,
            modifier = Modifier.weight(1f),
            onMedicineClick = onMedicineClick
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DiseaseContentPager(
    pages: List<String>,
    pagerState: PagerState,
    disease: DiseaseView,
    modifier: Modifier = Modifier,
    onMedicineClick: (String) -> Unit
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        pageCount = pages.size,
        pageSpacing = 8.dp
    ) { page: Int ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (page) {
                0 -> DiseaseDescription(description = disease.description, name = disease.name)
                1 -> DiseaseSymptoms(symptoms = disease.symptoms)
                2 -> DiseaseTreatment(treatment = disease.treatment)
                3 -> DiseasePrevention(prevention = disease.prevention)
                4 -> DiseaseDiagnosis(diagnosis = disease.diagnosis)
                5 -> DiseaseMedicines(
                    medicines = disease.medicines,
                    onMedicineClick = onMedicineClick
                )
            }
        }
    }
}

@Composable
private fun DiseaseDescription(name: String, description: String) {
    Text(text = name, style = MaterialTheme.typography.headlineLarge)
    Text(text = description, style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun DiseaseSymptoms(symptoms: List<String>) {
    Text(text = "Symptoms", style = MaterialTheme.typography.headlineMedium)
    Text(text = symptoms.joinToString(SEPARATOR), style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun DiseaseTreatment(treatment: List<String>) {
    Text(text = "Treatment", style = MaterialTheme.typography.headlineMedium)
    Text(text = treatment.joinToString(SEPARATOR), style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun DiseasePrevention(prevention: List<String>) {
    Text(text = "Prevention", style = MaterialTheme.typography.headlineMedium)
    Text(text = prevention.joinToString(SEPARATOR), style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun DiseaseDiagnosis(diagnosis: List<String>) {
    Text(text = "Diagnosis", style = MaterialTheme.typography.headlineMedium)
    Text(text = diagnosis.joinToString(SEPARATOR), style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun DiseaseMedicines(medicines: List<Medicine>, onMedicineClick: (String) -> Unit) {
    Text(text = "Medicines", style = MaterialTheme.typography.headlineMedium)
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(medicines) { medicine ->
            MedicineItem(
                medicine = medicine,
                onMedicineClick = onMedicineClick,
                modifier = Modifier.heightIn(max = 350.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineItem(
    medicine: Medicine,
    onMedicineClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = { onMedicineClick(medicine.id) },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(text = medicine.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = medicine.uses.firstOrNull()?:"", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiseaseScreenPreview() {
    DiseaseScreenContent(
        disease = DiseaseView.headache(),
        onMedicineClick = {}
    )
}