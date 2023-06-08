package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.models.diagnosis.DiagnosisMedicineCrossRef
import com.example.database.models.disease.DiseaseMedicineCrossRef
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.medicine.MedicineEntityView
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines")
    fun getMedicines(): DataSource.Factory<Int, MedicineEntity>

    @Query("SELECT * FROM medicines WHERE isCreated = 1")
    @Transaction
    suspend fun getCreatedMedicines(): List<MedicineEntityView>

    @Insert
    suspend fun insertDiagnosisCrossRefs(crossRefs: List<DiseaseMedicineCrossRef>)

    @Insert
    suspend fun insertDiseaseCrossRefs(crossRefs: List<DiagnosisMedicineCrossRef>)

    @Query("DELETE FROM diagnosisMedicinesCrossRef WHERE medicineId = :id")
    suspend fun deleteCrossRefs(id: String)

    @Query("select * from diagnosisMedicinesCrossRef where medicineId = :id")
    suspend fun getDiagnosisMedicineCrossRefs(id: String): List<DiagnosisMedicineCrossRef>

    @Query("UPDATE transactions SET medicineId = :newKey WHERE medicineId = :oldKey")
    suspend fun updateTransactionMedicineIds(oldKey: String, newKey: String)


    @Query("SELECT * FROM medicines WHERE id = :id")
    @Transaction
    fun getMedicine(id: String): Flow<MedicineEntityView?>

    @Query("DELETE FROM medicines")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(medicine: MedicineEntity, crossRefs: List<DiseaseMedicineCrossRef> = emptyList())

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<MedicineEntity>, crossRefs: List<DiseaseMedicineCrossRef> = emptyList())

    @Query("SELECT * FROM medicines")
    @Transaction
    fun getMedicinesFlow(): Flow<List<MedicineEntityView>>
}