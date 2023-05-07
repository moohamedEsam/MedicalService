package com.example.data.disease

import androidx.paging.PagingSource
import com.example.common.models.Result
import com.example.database.models.disease.DiseaseEntity
import com.example.database.models.disease.DiseaseEntityView
import com.example.database.models.disease.toDisease
import com.example.database.models.disease.toDiseaseView
import com.example.database.models.disease.toEntity
import com.example.database.room.dao.DiseaseDao
import com.example.model.app.Disease
import com.example.model.app.DiseaseView
import com.example.model.app.Symptom
import com.example.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single([DiseaseRepository::class])
class OfflineFirstDiseaseRepository(
    private val localDataSource: DiseaseDao,
    private val remoteDataSource: RemoteDataSource
) : DiseaseRepository {
    override fun getDiseaseDetails(diseaseId: String): Flow<DiseaseView> =
        localDataSource.getDisease(diseaseId).filterNotNull().map(DiseaseEntityView::toDiseaseView)

    override fun getDiseases(): PagingSource<Int, Disease> = localDataSource.getDiseases()
        .map(DiseaseEntity::toDisease)
        .asPagingSourceFactory()
        .invoke()

    override suspend fun predictDiseaseBySymptoms(symptoms: List<Symptom>): Result<DiseaseView> =
        remoteDataSource.predictDiseaseBySymptoms(symptoms)

    override fun getAvailableSymptoms(): Flow<List<Symptom>> =
        localDataSource.getSymptoms().map { names -> names.map { Symptom(it) } }

    override suspend fun syncDiseases(): Boolean {
        val diseases = remoteDataSource.getDiseases()
        diseases.ifSuccess {
            localDataSource.deleteAll()
            localDataSource.insertAll(it.map(Disease::toEntity))
        }
        return diseases is Result.Success
    }
}