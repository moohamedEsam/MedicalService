package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.models.disease.DiseaseMedicineCrossRef
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.medicine.MedicineEntityView
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines")
    fun getMedicines(): DataSource.Factory<Int, MedicineEntity>

    @Query("SELECT * FROM medicines WHERE id = :id")
    @Transaction
    fun getMedicine(id: String): Flow<MedicineEntityView?>

    @Query("DELETE FROM medicines")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(medicine: MedicineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<MedicineEntity>, crossRefs: List<DiseaseMedicineCrossRef> = emptyList())
}