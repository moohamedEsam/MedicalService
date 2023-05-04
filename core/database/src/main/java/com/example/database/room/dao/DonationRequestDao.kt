package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.database.models.DonationRequestEntity
import com.example.database.models.DonationRequestEntityView

@Dao
interface DonationRequestDao {

    @Query("SELECT * FROM donationRequests")
    suspend fun getDonationRequest(): DataSource.Factory<Int, DonationRequestEntity>

    @Query("SELECT * FROM donationRequests WHERE id = :id")
    suspend fun getDonationRequestById(id: String): DonationRequestEntityView?

    @Insert
    suspend fun insert(donationRequest: DonationRequestEntity)

    @Query("DELETE FROM donationRequests WHERE id = :id")
    suspend fun delete(id: String)
}