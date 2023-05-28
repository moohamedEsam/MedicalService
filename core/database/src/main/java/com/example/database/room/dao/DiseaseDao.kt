package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.models.disease.DiseaseEntity
import com.example.database.models.disease.DiseaseEntityView
import com.example.database.models.disease.DiseaseMedicineCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface DiseaseDao {
    @Query("SELECT * FROM diseases")
    fun getDiseases(): DataSource.Factory<Int, DiseaseEntity>

    @Query("SELECT * FROM diseases")
    @Transaction
    fun getDiseasesFlow(): Flow<List<DiseaseEntityView>>

    @Query("SELECT * FROM diseases WHERE id = :id")
    @Transaction
    fun getDisease(id: String): Flow<DiseaseEntityView?>

    @Query("SELECT * FROM symptoms")
    fun getSymptoms(): Flow<List<String>>

    @Insert
    fun insert(disease: DiseaseEntity)



    @Insert
    fun insertAll(diseases: List<DiseaseEntity>, crossRefs: List<DiseaseMedicineCrossRef> = emptyList())

    @Query("DELETE FROM diseases")
    fun deleteAll()
}