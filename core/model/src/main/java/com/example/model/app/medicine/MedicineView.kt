package com.example.model.app.medicine

import com.example.model.app.Disease
import com.example.model.app.empty

data class MedicineView(
    val name: String,
    val uses: List<String>,
    val diseases: List<Disease>,
    val sideEffects: List<String>,
    val precautions: List<String>,
    val overDoes: List<String>,
    val id: String,
) {
    companion object
}

fun MedicineView.Companion.empty() = MedicineView(
    name = "",
    diseases = emptyList(),
    sideEffects = emptyList(),
    precautions = emptyList(),
    overDoes = emptyList(),
    id = "",
    uses = emptyList(),
)

fun MedicineView.Companion.paracetamol() = MedicineView(
    name = "Paracetamol",
    diseases = listOf(
        Disease.empty().copy(name = "Headache"),
        Disease.empty().copy(name = "Fever"),
        Disease.empty().copy(name = "Toothache"),
    ),
    sideEffects = listOf(
        "Nausea",
        "Vomiting",
        "Stomach pain",
    ),
    precautions = listOf(
        "Pregnancy",
        "Breastfeeding",
        "Liver disease",
    ),
    uses = listOf("Paracetamol is a painkiller used to treat mild to moderate pain. It is also used to treat fever and headaches. It is available on prescription as tablets, capsules, and liquid. It is also available to buy from pharmacies without a prescription as tablets, capsules, and liquid. Paracetamol is also available as a soluble powder to make a liquid to drink."),
    overDoes = listOf("If you take too much paracetamol, you may get side effects such as feeling sick, being sick, stomach pain, loss of appetite, sweating, a fast heart rate, and confusion. These side effects are more likely if you take more than 8 tablets in 24 hours. If you take too much paracetamol, you may get side effects such as feeling sick, being sick, stomach pain, loss of appetite, sweating, a fast heart rate, and confusion. These side effects are more likely if you take more than 8 tablets in 24 hours."),
    id = "1",
)