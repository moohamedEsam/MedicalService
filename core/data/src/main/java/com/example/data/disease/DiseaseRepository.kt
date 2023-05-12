package com.example.data.disease

import androidx.paging.PagingSource
import com.example.common.models.Result
import com.example.model.app.disease.Disease
import com.example.model.app.disease.DiseaseView
import com.example.model.app.disease.Symptom
import kotlinx.coroutines.flow.Flow

interface DiseaseRepository {
    fun getDiseaseDetails(diseaseId: String): Flow<DiseaseView>

    fun getDiseases(): PagingSource<Int, Disease>

    suspend fun predictDiseaseBySymptoms(symptoms: List<Symptom>): Result<DiseaseView>

    fun getAvailableSymptoms(): Flow<List<Symptom>>

    suspend fun syncDiseases(): Boolean

}