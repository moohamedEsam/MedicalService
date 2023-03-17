package com.example.medicalservice.presentation.donation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.models.ValidationResult
import com.example.einvoicecomponents.OneTimeEventButton
import com.example.einvoicecomponents.textField.ValidationOutlinedTextField
import com.example.models.MedicineView
import com.example.models.empty
import com.example.models.paracetamol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun DonationScreen(
    viewModel: DonationViewModel = koinViewModel()
) {
    DonationScreen(
        medicines = viewModel.medicines,
        selectedMedicine = viewModel.selectedMedicine,
        quantity = viewModel.quantity,
        onQuantityChange = viewModel::onQuantityChanged,
        quantityValidationResult = viewModel.quantityValidationResult,
        onDonateClick = viewModel::onDonateClick,
        donateButtonEnabled = viewModel.isDonateEnabled,
        isLoading = viewModel.isLoading,
        onSelectMedicine = viewModel::onMedicineSelected
    )
}

@Composable
private fun DonationScreen(
    medicines: StateFlow<List<MedicineView>>,
    selectedMedicine: StateFlow<MedicineView?>,
    onSelectMedicine: (MedicineView) -> Unit,
    quantity: StateFlow<String>,
    onQuantityChange: (String) -> Unit,
    quantityValidationResult: StateFlow<ValidationResult>,
    onDonateClick: () -> Unit,
    donateButtonEnabled: StateFlow<Boolean>,
    isLoading: StateFlow<Boolean>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DonationHeader(
            medicines = medicines,
            selectedMedicine = selectedMedicine,
            modifier = Modifier
                .heightIn(max = (LocalConfiguration.current.screenHeightDp / 3).dp)
                .fillMaxWidth(),
            onSelectMedicine = onSelectMedicine
        )

        DonationBody(
            quantity = quantity,
            onQuantityChange = onQuantityChange,
            quantityValidationResult = quantityValidationResult,
            onDonateClick = onDonateClick,
            donateButtonEnabled = donateButtonEnabled,
            isLoading = isLoading
        )

    }
}

@Composable
private fun DonationHeader(
    medicines: StateFlow<List<MedicineView>>,
    selectedMedicine: StateFlow<MedicineView?>,
    onSelectMedicine: (MedicineView) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Medicine",
        style = MaterialTheme.typography.headlineMedium,
    )
    OutlinedCard(
        modifier = modifier.animateContentSize()
    ) {
        DonationHeaderBody(selectedMedicine, medicines, onSelectMedicine)
    }
}

@Composable
private fun DonationHeaderBody(
    selectedMedicineState: StateFlow<MedicineView?>,
    medicines: StateFlow<List<MedicineView>>,
    onSelectMedicine: (MedicineView) -> Unit
) {
    val selectedMedicine by selectedMedicineState.collectAsState()
    if (selectedMedicine != null)
        SelectedMedicineBox(selectedMedicine!!)
    else
        MedicineList(medicines, onSelectMedicine)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineList(
    medicinesState: StateFlow<List<MedicineView>>,
    onSelectMedicine: (MedicineView) -> Unit
) {
    val medicines by medicinesState.collectAsState()
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search"
            )
        },
        label = { Text(text = "Search") },
        modifier = Modifier
            .fillMaxWidth()
    )
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(medicines) { medicine ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectMedicine(medicine) }
            ) {
                Text(
                    text = medicine.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectedMedicineBox(selectedMedicine: MedicineView) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = selectedMedicine.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Diseases",
            style = MaterialTheme.typography.bodyLarge,
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(selectedMedicine.diseases) {
                SuggestionChip(onClick = { }, label = { Text(text = it.name) })
            }
        }
    }
}

@Composable
private fun ColumnScope.DonationBody(
    quantity: StateFlow<String>,
    onQuantityChange: (String) -> Unit,
    quantityValidationResult: StateFlow<ValidationResult>,
    onDonateClick: () -> Unit,
    donateButtonEnabled: StateFlow<Boolean>,
    isLoading: StateFlow<Boolean>,
) {
    ValidationOutlinedTextField(
        valueState = quantity,
        validationState = quantityValidationResult,
        label = "Quantity",
        onValueChange = onQuantityChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Medicine Image", style = MaterialTheme.typography.bodyLarge)
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.UploadFile,
                contentDescription = "Add a photo"
            )
        }
    }

    OneTimeEventButton(
        enabled = donateButtonEnabled,
        loading = isLoading,
        label = "Donate",
        onClick = onDonateClick,
        modifier = Modifier.align(Alignment.End)
    )
}

@Preview(showBackground = true)
@Composable
private fun DonationScreenPreview() {
    DonationScreen(
        medicines = MutableStateFlow(listOf(
            MedicineView.empty().copy(name = "Paracetamol"),
            MedicineView.empty().copy(name = "Ibuprofen"),
            MedicineView.empty().copy(name = "Aspirin"),
            MedicineView.empty().copy(name = "Caffeine"),
            MedicineView.empty().copy(name = "Codeine"),
            MedicineView.empty().copy(name = "Dextromethorphan"),
        ).sortedBy { it.name }),
        onDonateClick = {},
        modifier = Modifier.fillMaxSize(),
        selectedMedicine = MutableStateFlow(MedicineView.paracetamol()),
        quantity = MutableStateFlow("0"),
        onQuantityChange = {},
        quantityValidationResult = MutableStateFlow(ValidationResult.Valid),
        donateButtonEnabled = MutableStateFlow(false),
        isLoading = MutableStateFlow(false),
        onSelectMedicine = {}
    )
}