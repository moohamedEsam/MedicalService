package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.example.database.models.MedicineEntity
import com.example.database.models.MedicineEntityView

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines")
    suspend fun getMedicines(): DataSource.Factory<Int, MedicineEntity>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicine(id: String): MedicineEntityView?
}