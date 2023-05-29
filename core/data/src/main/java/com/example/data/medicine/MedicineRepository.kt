package com.example.data.medicine

import androidx.paging.PagingSource
import com.example.common.models.Result
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.MedicineView
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {

    suspend fun createMedicine(medicine: Medicine): Result<Medicine>
    fun getMedicineDetails(medicineId: String): Flow<MedicineView>

    fun getMedicines(): PagingSource<Int, Medicine>

    suspend fun syncMedicines(): Boolean
    fun getMedicinesFlow(): Flow<List<MedicineView>>
}