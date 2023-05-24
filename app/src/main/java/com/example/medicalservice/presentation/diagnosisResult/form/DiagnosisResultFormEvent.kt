package com.example.medicalservice.presentation.diagnosisResult.form


sealed interface DiagnosisResultFormEvent {
    object OnSaveClick : DiagnosisResultFormEvent
    object OnAddMedicineClick : DiagnosisResultFormEvent
    object OnAddUnRegisteredMedicineClick : DiagnosisResultFormEvent
    object OnAddDiseaseClick : DiagnosisResultFormEvent
    object OnAddUnRegisteredDiseaseClick : DiagnosisResultFormEvent
    object OnRemoveDiseaseClick : DiagnosisResultFormEvent
    object OnDiseaseClick : DiagnosisResultFormEvent
    data class OnMedicineClick(val medicineId: String) : DiagnosisResultFormEvent
    data class OnDiagnosisChange(val diagnosis: String) : DiagnosisResultFormEvent
    data class OnMedicineDelete(val medicineId: String) : DiagnosisResultFormEvent
    data class OnUnRegisteredMedicineDelete(val medicineName: String) : DiagnosisResultFormEvent

}