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

    sealed interface DiseaseOptionDialog : DiagnosisResultFormEvent {
        object Dismiss : DiseaseOptionDialog
        data class OnQueryChange(val query: String) : DiseaseOptionDialog
        data class OnDiseaseClick(val diseaseId: String) : DiseaseOptionDialog
    }

    sealed interface MedicineOptionDialog : DiagnosisResultFormEvent {
        object Dismiss : MedicineOptionDialog
        data class OnQueryChange(val query: String) : MedicineOptionDialog
        data class OnMedicineClick(val medicineId: String) : MedicineOptionDialog
    }

}