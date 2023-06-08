package com.example.medicalservice.presentation.diagnosisResult.form


sealed interface DiagnosisResultFormEvent {
    sealed interface Form : DiagnosisResultFormEvent {
        object OnSaveClick : Form
        object OnAddMedicineClick : Form
        object OnAddUnRegisteredMedicineClick : Form
        object OnAddDiseaseClick : Form
        object OnAddUnRegisteredDiseaseClick : Form
        object OnRemoveDiseaseClick : Form
        object OnDiseaseClick : Form
        data class OnUnRegisteredMedicineDelete(val medicineName: String) : Form
        data class OnMedicineClick(val medicineId: String) : Form
        data class OnDiagnosisChange(val diagnosis: String) : Form
        data class OnMedicineDelete(val medicineId: String) : Form
    }

    sealed interface UnregisteredDiseaseDialog : DiagnosisResultFormEvent {
        object Dismiss : UnregisteredDiseaseDialog
        data class OnDiseaseChange(val disease: String) : UnregisteredDiseaseDialog
        object OnSaveClick : UnregisteredDiseaseDialog
    }

    sealed interface UnregisteredMedicineDialog : DiagnosisResultFormEvent {
        object Dismiss : UnregisteredMedicineDialog
        data class OnMedicineChange(val medicine: String) : UnregisteredMedicineDialog
        object OnSaveClick : UnregisteredMedicineDialog
    }

    sealed interface DiseaseOptionSearch : DiagnosisResultFormEvent {
        object Dismiss : DiseaseOptionSearch
        data class OnQueryChange(val query: String) : DiseaseOptionSearch
        data class OnDiseaseClick(val diseaseId: String) : DiseaseOptionSearch
    }

    sealed interface MedicineOptionSearch : DiagnosisResultFormEvent {
        object Dismiss : MedicineOptionSearch
        data class OnQueryChange(val query: String) : MedicineOptionSearch
        data class OnMedicineClick(val medicineId: String) : MedicineOptionSearch
    }

}