package com.example.medicalservice.presentation.home.donner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medicalservice.presentation.components.transactionsList
import com.example.medicalservice.presentation.transaction.TransactionScreen
import com.example.models.*
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

//todo remove use resource instead of image vector
//private const val donateResource = R.drawable.donate

@Composable
fun DonnerHomeScreen(
    onMedicineClick: (String) -> Unit,
    onDonateClick: () -> Unit,
    viewModel: DonnerHomeViewModel = koinViewModel()
) {
    val user by viewModel.user.collectAsState()
    val mostNeededMedicine by viewModel.mostNeededMedicine.collectAsState()
    val showTransactionDialog by viewModel.showTransactionDialog.collectAsState()
    if (user !is User.Donor) return
    Box {
        DonnerHomeScreenContent(
            user = user as User.Donor,
            donationRequests = mostNeededMedicine,
            onTransactionClick = viewModel::onTransactionClick,
            onDonateClick = onDonateClick,
            onMedicineClick = onMedicineClick
        )
        TransactionSheet(
            showTransactionDialog = showTransactionDialog,
            onMedicineClick = onMedicineClick,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .align(Alignment.BottomCenter),
            transactionState = viewModel.transaction,
            onDismiss = viewModel::onTransactionDialogDismiss
        )
    }
}

@Composable
private fun TransactionSheet(
    showTransactionDialog: Boolean,
    onMedicineClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    transactionState: StateFlow<Transaction?>,
    onDismiss: () -> Unit
) {
    val transaction by transactionState.collectAsState()
    if (transaction == null) return
    AnimatedVisibility(
        visible = showTransactionDialog,
        enter = slideInVertically { it + 200 },
        exit = slideOutVertically { it + 200 },
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    if (delta > 0) {
                        onDismiss()
                    }
                }
            )
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Drag"
                )
            }
            TransactionScreen(
                transaction = transaction!!,
                onMedicineClick = onMedicineClick,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun DonnerHomeScreenContent(
    user: User.Donor,
    donationRequests: List<DonationRequest>,
    onTransactionClick: (Transaction) -> Unit,
    onMedicineClick: (String) -> Unit,
    onDonateClick: () -> Unit,
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            donnerScreenHeader(user)
            transactionsList(user.recentTransactions, onTransactionClick)
            activeDonationList(donationRequests, onMedicineClick)
        }

        FloatingActionButton(
            onClick = onDonateClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
//                painter = painterResource(id = donateResource),
                imageVector = Icons.Default.Settings,
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

private fun LazyListScope.activeDonationList(
    donationRequests: List<DonationRequest>,
    onMedicineClick: (String) -> Unit
) {
    item {
        Text(text = "Most Needed Medicine", style = MaterialTheme.typography.headlineSmall)
    }

    items(donationRequests) {
        DonationItem(
            donationRequest = it,
            onClick = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DonationItem(
    donationRequest: DonationRequest,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = donationRequest.medicine.name, style = MaterialTheme.typography.bodyLarge)

            Text(
                text = donationRequest.medicine.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
            val progress = donationRequest.collected.toFloat() / donationRequest.needed.toFloat()
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
                mainAxisAlignment = MainAxisAlignment.SpaceBetween,
                crossAxisAlignment = FlowCrossAxisAlignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        donationRequest.contributorsCount.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(imageVector = Icons.Default.Group, contentDescription = null)
                }

                Text(
                    "${donationRequest.collected}/${donationRequest.needed}",
                    style = MaterialTheme.typography.bodySmall
                )

                IconButton(
                    onClick = { },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
//                    painter = painterResource(id = donateResource),
                        imageVector = Icons.Default.Settings,
                        contentDescription = "donate"
                    )
                }
            }


        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DonnerHomeScreenPreview() {
    Box {
        DonnerHomeScreenContent(
            user = User.emptyDonor().copy(
                username = "John",
                recentTransactions = List(10) { Transaction.empty() }
            ),
            donationRequests = listOf(
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
            ).map {
                DonationRequest.empty().copy(
                    medicine = it,
                    needed = 100,
                    collected = Random.nextInt(0, 50),
                    contributorsCount = Random.nextInt(100)
                )
            },
            onTransactionClick = {},
            onDonateClick = {},
            onMedicineClick = {}
        )

        TransactionSheet(
            showTransactionDialog = false,
            onMedicineClick = {},
            transactionState = MutableStateFlow(Transaction.empty()),
            modifier = Modifier.align(Alignment.BottomStart),
            onDismiss = {}
        )
    }
}