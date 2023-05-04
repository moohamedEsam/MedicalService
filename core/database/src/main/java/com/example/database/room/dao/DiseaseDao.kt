package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.example.database.models.DiseaseEntity
import com.example.database.models.DiseaseEntityView

@Dao
interface DiseaseDao {
    @Query("SELECT * FROM diseases")
    suspend fun getDiseases(): DataSource.Factory<Int, DiseaseEntity>

    @Query("SELECT * FROM diseases WHERE id = :id")
    suspend fun getDisease(id: String): DiseaseEntityView?
}