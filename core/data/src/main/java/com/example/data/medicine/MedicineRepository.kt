package com.example.data.medicine

import androidx.paging.PagingSource
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.MedicineView
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    fun getMedicineDetails(medicineId: String): Flow<MedicineView>

    fun getMedicines(): PagingSource<Int, Medicine>

    suspend fun syncMedicines(): Boolean
}