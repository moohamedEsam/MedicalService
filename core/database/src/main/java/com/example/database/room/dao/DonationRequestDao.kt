package com.example.database.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.models.donation.DonationRequestEntity
import com.example.database.models.donation.DonationRequestEntityView
import kotlinx.coroutines.flow.Flow

@Dao
interface DonationRequestDao {

    @Query("SELECT * FROM donationRequests")
    @Transaction
    fun getDonationRequests(): DataSource.Factory<Int, DonationRequestEntityView>

    @Query("SELECT * FROM donationRequests WHERE isBookmarked = 1")
    @Transaction
    fun getBookmarkedDonationRequests(): DataSource.Factory<Int, DonationRequestEntityView>

    @Query("SELECT * FROM donationRequests WHERE id = :id")
    @Transaction
    fun getDonationRequestById(id: String): Flow<DonationRequestEntityView?>

    @Query("update donationRequests set isBookmarked = :isBookmarked where id = :id")
    suspend fun setDonationRequestBookmark(id: String, isBookmarked: Boolean)

    @Insert
    suspend fun insert(donationRequest: DonationRequestEntity)

    @Insert
    suspend fun insertAll(donationRequests: List<DonationRequestEntity>)

    @Query("DELETE FROM donationRequests WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM donationRequests")
    suspend fun deleteAll()
}