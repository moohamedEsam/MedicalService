package com.example.data.medicine

import androidx.paging.PagingSource
import com.example.common.models.Result
import com.example.database.models.medicine.MedicineEntityView
import com.example.database.models.medicine.toEntity
import com.example.database.models.medicine.toMedicine
import com.example.database.models.medicine.toMedicineView
import com.example.database.room.dao.MedicineDao
import com.example.model.app.Medicine
import com.example.model.app.MedicineView
import com.example.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single([MedicineRepository::class])
class OfflineFirstMedicineRepository(
    private val localDataSource: MedicineDao,
    private val remoteDataSource: RemoteDataSource
) : MedicineRepository {
    override fun getMedicineDetails(medicineId: String): Flow<MedicineView> =
        localDataSource.getMedicine(medicineId).filterNotNull().map(MedicineEntityView::toMedicineView)

    override fun getMedicines(): PagingSource<Int, Medicine> =
        localDataSource.getMedicines().map { it.toMedicine() }.asPagingSourceFactory().invoke()

    override suspend fun syncMedicines(): Boolean {
        val medicines = remoteDataSource.getMedicines()
        medicines.ifSuccess {
            localDataSource.deleteAll()
            val entities = it.map { med->med.toEntity() }
            localDataSource.insertAll(entities)
        }
        return medicines is Result.Success
    }
}