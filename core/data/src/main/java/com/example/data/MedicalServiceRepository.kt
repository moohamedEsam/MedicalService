package com.example.data

import com.example.common.models.Result
import com.example.model.app.DiseaseView
import com.example.model.app.DonationRequestView
import com.example.model.app.MedicineView
import com.example.model.app.Symptom
import com.example.model.app.TransactionView

sealed interface MedicalServiceRepository {
    suspend fun getAvailableSymptoms(): List<Symptom>
    suspend fun predictDiseaseBySymptoms(symptoms: List<Symptom>): Result<DiseaseView>
    suspend fun getDonationRequests(): List<DonationRequestView>
    suspend fun getCurrentUserTransactions(): List<TransactionView>
    suspend fun getDiseaseDetails(diseaseId: String): Result<DiseaseView>
    suspend fun getMedicineDetails(medicineId: String): Result<MedicineView>
    suspend fun getMedicines(): List<MedicineView>
    suspend fun getDiseases(): List<DiseaseView>
}