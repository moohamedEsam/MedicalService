package com.example.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.models.diagnosis.DiagnosisRequestEntity
import com.example.database.models.diagnosis.DiagnosisResultEntity
import com.example.database.models.diagnosis.DiagnosisResultEntityView
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisResultDao {

    @Query("SELECT * FROM diagnosisResults ORDER BY createdAt DESC LIMIT 1")
    @Transaction
    fun getLatestDiagnosisResult(): Flow<DiagnosisResultEntityView?>

    @Query("SELECT * FROM diagnosisResults ORDER BY createdAt DESC")
    fun getDiagnosisResults(): Flow<List<DiagnosisResultEntity>>

    @Query("SELECT * FROM diagnosisResults WHERE id = :id")
    @Transaction
    fun getDiagnosisResult(id: String): Flow<DiagnosisResultEntityView?>

    @Query("SELECT * FROM diagnosisResults WHERE isCreated = 1")
    suspend fun getCreatedDiagnosisResults(): List<DiagnosisResultEntity>

    @Query("SELECT * FROM diagnosisResults WHERE isUpdated = 1 and isCreated = 0")
    suspend fun getUpdatedDiagnosisResults(): List<DiagnosisResultEntity>

    @Query("DELETE FROM diagnosisResults")
    suspend fun deleteAllDiagnosisResults()

    @Query("DELETE FROM diagnosisResults where id = :id")
    suspend fun deleteDiagnosisResult(id: String)

    @Insert
    suspend fun insert(diagnosisResult: DiagnosisResultEntity)

    @Insert
    suspend fun insertAll(diagnosisResults: List<DiagnosisResultEntity>)

    @Update
    suspend fun update(diagnosisResult: DiagnosisResultEntity)
}