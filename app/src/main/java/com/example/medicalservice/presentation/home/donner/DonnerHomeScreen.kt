package com.example.medicalservice.presentation.home.donner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medicalservice.R
import com.example.medicalservice.presentation.components.transactionsList
import com.example.models.*
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random


private const val donateResource = R.drawable.donate

@Composable
fun DonnerHomeScreen(
    onMedicineClick: (String) -> Unit,
    onDonateClick: () -> Unit,
    viewModel: DonnerHomeViewModel = koinViewModel()
) {
    val user by viewModel.user.collectAsState()
    val mostNeededMedicine by viewModel.mostNeededMedicine.collectAsState()
    if (user !is User.Donor) return
    DonnerHomeScreenContent(
        user = user as User.Donor,
        mostNeededMedicine = mostNeededMedicine,
        onMedicineClick = onMedicineClick,
        onDonateClick = onDonateClick
    )
}

@Composable
private fun DonnerHomeScreenContent(
    user: User.Donor,
    mostNeededMedicine: List<Medicine>,
    onMedicineClick: (String) -> Unit,
    onDonateClick: () -> Unit
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            donnerScreenHeader(user)
            transactionsList(user.recentTransactions, onMedicineClick)
            mostNeededMedicineList(mostNeededMedicine, onMedicineClick)
        }

        FloatingActionButton(
            onClick = onDonateClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(id = donateResource),
                contentDescription = "Donate",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun LazyListScope.donnerScreenHeader(
    user: User.Donor
) {
    item {
        Text(text = "Hello ${user.username}", style = MaterialTheme.typography.headlineMedium)
    }
}

private fun LazyListScope.mostNeededMedicineList(
    medicines: List<Medicine>,
    onMedicineClick: (String) -> Unit
) {
    item {
        Text(text = "Most Needed Medicine", style = MaterialTheme.typography.headlineSmall)
    }

    items(medicines) {
        MedicineItem(
            medicine = it,
            quantity = Random.nextInt(),
            onClick = { onMedicineClick(it.id) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineItem(
    medicine: Medicine,
    quantity: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 4.dp,
                mainAxisAlignment = MainAxisAlignment.SpaceBetween
            ) {
                Text(text = medicine.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Quantity:$quantity", style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = medicine.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
            IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.End)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = donateResource),
                    contentDescription = "donate"
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DonnerHomeScreenPreview() {
    DonnerHomeScreenContent(
        user = User.emptyDonor().copy(
            username = "John",
            recentTransactions = List(10) { Transaction.empty() }
        ),
        mostNeededMedicine = listOf(
            Medicine.empty().copy(
                name = "Paracetamol",
                description = "Paracetamol is a medicine used to treat pain and fever. It is typically used for mild to moderate conditions. Paracetamol is generally as effective as other painkillers but has fewer side effects. It is available on prescription and over the counter. It is also available as a generic medicine. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type of analgesic (painkiller) and antipyretic (fever reducer). It works by blocking the production of certain chemicals in the body that cause pain and fever. Paracetamol is a type"
            ),
            Medicine.empty().copy(name = "Ibuprofen"),
            Medicine.empty().copy(name = "Aspirin"),
            Medicine.empty().copy(name = "Cetirizine"),
            Medicine.empty().copy(name = "Diphenhydramine"),
            Medicine.empty().copy(name = "Loratadine"),
            Medicine.empty().copy(name = "Fexofenadine"),
        ),
        onMedicineClick = {},
        onDonateClick = {}
    )
}