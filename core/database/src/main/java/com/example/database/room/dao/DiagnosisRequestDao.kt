package com.example.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.database.models.diagnosis.DiagnosisRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisRequestDao {

    @Query("SELECT * FROM diagnosisRequests ORDER BY date DESC LIMIT 1")
    fun getLatestDiagnosisRequest(): Flow<DiagnosisRequestEntity?>

    @Query("SELECT * FROM diagnosisRequests ORDER BY date DESC")
    fun getDiagnosisRequestsFlow(): Flow<List<DiagnosisRequestEntity>>

    @Query("SELECT * FROM diagnosisRequests WHERE id = :id")
    fun getDiagnosisRequest(id: String): Flow<DiagnosisRequestEntity?>

    @Query("SELECT * FROM diagnosisRequests WHERE isCreated = 1")
    suspend fun getCreatedDiagnosisRequests(): List<DiagnosisRequestEntity>

    @Query("SELECT * FROM diagnosisRequests WHERE isUpdated = 1 and isCreated = 0")
    suspend fun getUpdatedDiagnosisRequests(): List<DiagnosisRequestEntity>


    @Query("DELETE FROM diagnosisRequests")
    suspend fun deleteAllDiagnosisRequests()

    @Query("DELETE FROM diagnosisRequests where id = :id")
    suspend fun deleteDiagnosisRequest(id: String)

    @Insert
    suspend fun insert(diagnosisRequestEntity: DiagnosisRequestEntity)

    @Insert
    suspend fun insertAll(diagnosisRequestEntities: List<DiagnosisRequestEntity>)

    @Update
    suspend fun update(diagnosisRequestEntity: DiagnosisRequestEntity)

}