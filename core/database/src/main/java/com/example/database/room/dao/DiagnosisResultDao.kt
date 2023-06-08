package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.models.diagnosis.DiagnosisMedicineCrossRef
import com.example.database.models.diagnosis.DiagnosisResultEntity
import com.example.database.models.diagnosis.DiagnosisResultEntityView
import com.example.model.app.diagnosis.DiagnosisResult
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisResultDao {

    @Query("SELECT * FROM diagnosisResults where status != :completeStatus ORDER BY createdAt DESC")
    @Transaction
    fun getDiagnosisResultsView(completeStatus: DiagnosisResult.Status = DiagnosisResult.Status.Complete): DataSource.Factory<Int, DiagnosisResultEntityView>

    @Query("SELECT * FROM diagnosisResults ORDER BY createdAt DESC LIMIT 1")
    @Transaction
    fun getLatestDiagnosisResult(): Flow<DiagnosisResultEntityView?>

    @Query("SELECT * FROM diagnosisResults ORDER BY createdAt DESC")
    fun getDiagnosisResults(): Flow<List<DiagnosisResultEntity>>

    @Query("SELECT * FROM diagnosisResults WHERE id = :id")
    @Transaction
    fun getDiagnosisResult(id: String): Flow<DiagnosisResultEntityView?>

    @Query("SELECT * FROM diagnosisResults WHERE isCreated = 1")
    @Transaction
    suspend fun getCreatedDiagnosisResults(): List<DiagnosisResultEntityView>

    @Query("SELECT * FROM diagnosisResults WHERE isUpdated = 1 and isCreated = 0")
    @Transaction
    suspend fun getUpdatedDiagnosisResults(): List<DiagnosisResultEntityView>

    @Query("DELETE FROM diagnosisResults")
    suspend fun deleteAllDiagnosisResults()

    @Query("DELETE FROM diagnosisResults where id = :id")
    suspend fun deleteDiagnosisResult(id: String)

    @Insert(onConflict = REPLACE)
    suspend fun insert(diagnosisResult: DiagnosisResultEntity, crossRefs: List<DiagnosisMedicineCrossRef> = emptyList())

    @Insert(onConflict = REPLACE)
    suspend fun insertCrossRefs(crossRefs: List<DiagnosisMedicineCrossRef>)

    @Insert
    suspend fun insertAll(diagnosisResults: List<DiagnosisResultEntity>)

    @Update(onConflict = REPLACE)
    suspend fun update(diagnosisResult: DiagnosisResultEntity)
}