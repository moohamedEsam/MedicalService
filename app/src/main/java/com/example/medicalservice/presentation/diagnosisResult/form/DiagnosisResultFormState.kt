package com.example.medicalservice.presentation.diagnosisResult.form

import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.empty
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.empty


data class DiagnosisResultFormState(
    val diagnosisRequest: DiagnosisRequest = DiagnosisRequest.empty(),
    val disease: DiseaseView? = DiseaseView.empty(),
    val medicationsIds: List<String> = emptyList(),
    val unRegisteredMedicines: List<String> = emptyList(),
    val isDiseaseInDatabase: Boolean = true,
    val diagnosis: String = "",
    val diseaseOptions: List<DiseaseView> = emptyList(),
    val isDiseaseSearchBarVisible: Boolean = false,
    val diseaseOptionsSearchQuery: String = "",
    val isMedicineOptionSearchVisible: Boolean = false,
    val medicineOptionSearchQuery: String = "",
    val isUnregisteredDiseaseDialogVisible: Boolean = false,
    val unregisteredDiseaseValue: String = "",
    val isUnregisteredMedicineDialogVisible: Boolean = false,
    val unregisteredMedicineValue: String = "",
) {
    val isAddDiseaseVisible = disease == null

    val filteredDiseaseOptions =
        if (diseaseOptionsSearchQuery.isBlank()) diseaseOptions
        else diseaseOptions.filter { it.name.contains(diseaseOptionsSearchQuery, true) }

    val filteredMedicineOptions = buildList {
        if (disease == null) return@buildList
        val suggestions = disease.medicines.filterNot { it.id in medicationsIds }
        if (medicineOptionSearchQuery.isBlank())
            addAll(suggestions)
        else
            addAll(suggestions.filter { it.name.contains(medicineOptionSearchQuery, true) })

    }


    val medications = disease?.medicines?.filter { it.id in medicationsIds } ?: emptyList()

    val isSaveButtonEnabled = disease != null && diagnosis.isNotBlank() && medications.map { it.id }
        .plus(unRegisteredMedicines).isNotEmpty()
}
