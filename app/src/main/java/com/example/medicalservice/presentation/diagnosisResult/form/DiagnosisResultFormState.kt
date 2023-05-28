package com.example.medicalservice.presentation.diagnosisResult.form

import com.example.model.app.diagnosis.DiagnosisRequest
import com.example.model.app.diagnosis.empty
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.empty
import com.example.model.app.medicine.Medicine


data class DiagnosisResultFormState(
    val diagnosisRequest: DiagnosisRequest = DiagnosisRequest.empty(),
    val disease: DiseaseView? = DiseaseView.empty(),
    val medicationsIds: List<String> = emptyList(),
    val unRegisteredMedicines: List<String> = emptyList(),
    val isDiseaseInDatabase: Boolean = true,
    val diagnosis: String = "",
    val diseaseOptions: List<DiseaseView> = emptyList(),
    val isDiseaseOptionDialogVisible: Boolean = false,
    val diseaseOptionDialogSearchQuery: String = "",
    val isMedicineOptionDialogVisible: Boolean = false,
    val medicineOptionDialogSearchQuery: String = "",
    val isUnregisteredDiseaseDialogVisible: Boolean = false,
    val unregisteredDiseaseValue: String = "",
    val isUnregisteredMedicineDialogVisible: Boolean = false,
    val unregisteredMedicineValue: String = "",
) {
    val isAddDiseaseVisible = disease == null

    val medications = disease?.medicines?.filter { it.id in medicationsIds } ?: emptyList()

    val isSaveButtonEnabled = disease != null && diagnosis.isNotBlank() && medications.map { it.id }
        .plus(unRegisteredMedicines).isNotEmpty()
}
