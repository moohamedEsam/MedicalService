package com.example.data.medicine

import androidx.paging.PagingSource
import com.example.common.functions.tryWrapper
import com.example.common.models.Result
import com.example.database.models.disease.DiseaseMedicineCrossRef
import com.example.database.models.medicine.MedicineEntityView
import com.example.database.models.medicine.toEntity
import com.example.database.models.medicine.toMedicine
import com.example.database.models.medicine.toMedicineView
import com.example.database.room.dao.MedicineDao
import com.example.model.app.medicine.Medicine
import com.example.model.app.medicine.MedicineView
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
    override suspend fun createMedicine(medicine: Medicine): Result<Medicine> = tryWrapper {
        localDataSource.insert(
            medicine.toEntity().copy(isCreated = true),
            medicine.diseasesId.map { DiseaseMedicineCrossRef(it, medicine.id) }
        )

        Result.Success(medicine)
    }

    override fun getMedicinesFlow(): Flow<List<MedicineView>> = localDataSource.getMedicinesFlow()
        .map { it.map(MedicineEntityView::toMedicineView) }

    override fun getMedicineDetails(medicineId: String): Flow<MedicineView> =
        localDataSource.getMedicine(medicineId).filterNotNull()
            .map(MedicineEntityView::toMedicineView)

    override fun getMedicines(): PagingSource<Int, Medicine> =
        localDataSource.getMedicines().map { it.toMedicine() }.asPagingSourceFactory().invoke()

    override suspend fun syncMedicines(): Boolean {
        val createdMedicines = localDataSource.getCreatedMedicines()
        createdMedicines.forEach { oldMedicine ->
            val result = remoteDataSource.createMedicine(oldMedicine.toMedicine())
            result.ifSuccess { medicine ->
                val crossRefs =
                    localDataSource.getDiagnosisMedicineCrossRefs(oldMedicine.medicineEntity.id)
                        .map { ref -> ref.copy(medicineId = medicine.id) }

                localDataSource.insertDiseaseCrossRefs(crossRefs)
                localDataSource.deleteCrossRefs(oldMedicine.medicineEntity.id)
                localDataSource.updateTransactionMedicineIds(
                    oldMedicine.medicineEntity.id,
                    medicine.id
                )
            }
        }

        val result = remoteDataSource.getMedicines()
        result.ifSuccess { medicines ->
            val entities = medicines.map { med -> med.toEntity() }
            val crossRefs = buildList {
                for (med in medicines) {
                    val refs = med.diseasesId
                        .map { id -> DiseaseMedicineCrossRef(id, med.id) }

                    addAll(refs)
                }
            }
            localDataSource.insertAll(entities, crossRefs)
        }
        return result is Result.Success
    }
}