package com.example.medicalservice.presentation.home.receiver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medicalservice.presentation.components.transactionsList
import com.example.models.*
import com.example.models.app.Disease
import com.example.models.app.Medicine
import com.example.models.app.Transaction
import com.example.models.app.User
import com.example.models.app.empty
import com.example.models.app.emptyReceiver
import org.koin.androidx.compose.koinViewModel
import java.util.*

@Composable
fun ReceiverHomeScreen(
    viewModel: ReceiverHomeViewModel = koinViewModel(),
    onDiseaseClick: (String) -> Unit,
    onMedicineClick: (String) -> Unit
) {
    val user by viewModel.user.collectAsState()
    if (user is User.Receiver)
        ReceiverHomeScreenContent(
            user = user as User.Receiver,
            onDiseaseClick = onDiseaseClick,
            onMedicineClick = onMedicineClick
        )
}


@Composable
private fun ReceiverHomeScreenContent(
    user: User.Receiver,
    onDiseaseClick: (String) -> Unit,
    onMedicineClick: (String) -> Unit
) {
    var expandButton by remember {
        mutableStateOf(false)
    }

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            expandButton = available.y > 0 || consumed.y > 0
            return super.onPostScroll(consumed, available, source)
        }
    }
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            receiverHomeScreenHeader(user = user)
            receiverHomeScreenBody(
                user = user,
                onDiseaseClick = onDiseaseClick,
                onMedicineClick = onMedicineClick
            )
        }
        ExtendedFloatingActionButton(
            onClick = { },
            text = { Text(text = "Apply for a new medicine") },
            icon = { Icon(imageVector = Icons.Default.Upload, contentDescription = null) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            expanded = expandButton
        )
    }
}

private fun LazyListScope.receiverHomeScreenHeader(user: User.Receiver) {
    item {
        Text(
            text = "Hello, ${user.username}",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

private fun LazyListScope.receiverHomeScreenBody(
    user: User.Receiver,
    onDiseaseClick: (String) -> Unit,
    onMedicineClick: (String) -> Unit
) {
    item {
        Text(text = "Your diseases", style = MaterialTheme.typography.headlineSmall)
    }
    items(user.diseases) {
        DiseaseItem(
            disease = it,
            onClick = { onDiseaseClick(it.id) }
        )
    }

//    transactionsList(user.recentTransactions) { } //todo add transaction sheet
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiseaseItem(
    disease: Disease,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = disease.name, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = disease.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReceiverHomeScreenPreview() {
    ReceiverHomeScreenContent(
        user = User.emptyReceiver().copy(
            diseases = listOf(
                Disease.empty().copy(
                    name = "Cancer",
                    description = "Cancer is a group of diseases involving abnormal cell growth with the potential to invade or spread to other parts of the body. These contrast with benign tumors, which do not spread. Possible signs and symptoms include a lump, abnormal bleeding, prolonged cough, unexplained weight loss, and a change in bowel movements. While these symptoms may indicate cancer, they may have other causes. Over 100 types of cancers affect humans. More than half of cancers occur in people over the age of 65 years. Smoking, diet, obesity, lack of exercise, and use of alcohol are known to increase the risk of cancer. Infections such as Helicobacter pylori and human papillomavirus may play a role. About 10–15% of cancers are due to inherited genetic defects, with the remainder due to unknown causes. Most cancers are diagnosed by a combination of imaging tests, biopsies, and blood tests. Treatment may involve surgery, radiation therapy, chemotherapy, immunotherapy, targeted therapy, or bone marrow transplantation. Less common treatments include stem cell transplantation, cryotherapy, and hormone therapy. Supportive care may be used to treat side effects. Palliative care may be appropriate in people with advanced cancer. Life expectancy depends on the type of cancer and the stage of the disease. Early detection through screening tests and regular checkups can increase the chance of successful treatment. The word cancer comes from the Latin word for crab, as cancers are able to spread throughout the body in this manner. The disease is not limited to humans, as the crab also has a similar ability to spread throughout the water."
                ),
                Disease.empty().copy(
                    name = "Diabetes",
                    description = "Diabetes mellitus, commonly referred to as diabetes, is a group of metabolic disorders in which there are high blood sugar levels over a prolonged period. Symptoms of high blood sugar include frequent urination, increased thirst, and increased hunger. If left untreated, diabetes can cause many complications. Acute complications can include diabetic ketoacidosis, hyperosmolar hyperglycemic state, or death. Serious long-term complications include cardiovascular disease, stroke, chronic kidney disease, foot ulcers, and damage to the eyes. Diabetes is due to either the pancreas not producing enough insulin or the cells of the body not responding properly to the insulin produced. There is also a form of diabetes mellitus that results from a combination of insulin resistance and inadequate insulin production. Diabetes is a major cause of blindness, kidney failure, heart attacks, strokes, and lower limb amputations. It was the seventh leading cause of death in the United States in 2018. The global economic cost of diabetes in 2014 was estimated to be US$673 billion. In the United States, diabetes cost $327 billion in 2017. The prevalence of diabetes is increasing rapidly and has reached epidemic proportions globally. In 2019, an estimated 463 million people had diabetes worldwide, with type 2 diabetes making up about 90% of the cases. This represents 8.8% of the adult population, up from just 4.7% in 1980. Type 1 diabetes is less common, but is usually more severe. The global prevalence of diabetes is expected to increase by 2050. In the United States, diabetes increased from 5.58 million in 1980 to 34.2 million in 2015. In 2015, Type 2 diabetes accounted for 90–95% of all cases of diabetes in adults. In the United States, 41% of adults with diabetes are diagnosed, while 18% are undiagnosed. Diabetes is more common in low- and middle-income countries but less common in East Asia and the Pacific. The global prevalence of diabetes might be overestimated due to the wide availability of diagnostic testing. Diabetes is a major cause of death in the United States. In 2015, diabetes was the seventh leading cause of death in the United States with 250,000 deaths. Diabetes was the sixth leading cause of death in 2005. The risk of death from diabetes is increased by 50"
                ),
                Disease.empty().copy(
                    name = "Hypertension",
                    description = "Hypertension, also known as high blood pressure, is a long-term medical condition in which the blood pressure in the arteries is persistently elevated. High blood pressure typically does not cause symptoms. Long-term high blood pressure, however, is a major risk factor for coronary artery disease, stroke, heart failure, atrial fibrillation, peripheral vascular disease, vision loss, chronic kidney disease, and dementia. Hypertension is classified using the following ranges, with the most common range being 130–139 mmHg systolic and 80–89 mmHg diastolic: Mild hypertension: 130–139/80–89 mmHg Moderate hypertension: 140–159/90–99 mmHg Severe hypertension: 160–179/100–109 mmHg Very severe hypertension: 180/110 mmHg or higher Hypertension is usually classified as primary or secondary. Primary hypertension is more common and is linked to lifestyle factors and genetics. Secondary hypertension is due to an identifiable cause, such as kidney disease, and is more common in the elderly. Hypertension is diagnosed by taking an average of two or more blood pressure readings at three or more separate health care visits. The diagnosis may be confirmed with ambulatory blood pressure monitoring over 24 hours. If the blood pressure is not controlled with medications, procedures to reduce blood pressure may be recommended. These include lifestyle changes, medication, and possibly surgery. The goal of treatment is to reduce blood pressure to less than 130/80 mmHg. In those with diabetes or chronic kidney disease, the goal is to reduce blood pressure to less than 130/80 mmHg. For those with chronic kidney disease, the goal is to reduce blood pressure to less than 130/80 mmHg. For those with diabetes or chronic kidney disease, the goal is to reduce blood pressure to less than 130/80 mmHg. For those with chronic kidney disease, the goal is to reduce blood pressure to less than 130/80 mmHg. For those with diabetes or chronic kidney disease, the goal is to reduce blood pressure to less than 130/80 mmHg. For those with chronic kidney disease, the goal is to reduce blood pressure to less than 130/80 mmHg. For those with diabetes or chronic kidney disease, the goal is to reduce blood pressure to less than 130/80 mmHg. For)"
                ),
                Disease.empty().copy(name = "Asthma")
            ),
            suggestedMedicines = listOf(
                Medicine.empty().copy(name = "Paracetamol"),
                Medicine.empty().copy(name = "Aspirin"),
                Medicine.empty().copy(name = "Ibuprofen"),
                Medicine.empty().copy(name = "Diphenhydramine"),
            ),
            username = "John Doe",
        ),
        onMedicineClick = { },
        onDiseaseClick = { },
    )
}