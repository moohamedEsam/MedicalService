package com.example.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.models.disease.DiseaseEntity
import com.example.database.models.disease.DiseaseMedicineCrossRef
import com.example.database.models.disease.SymptomEntity
import com.example.database.models.donation.DonationRequestEntity
import com.example.database.models.medicine.MedicineEntity
import com.example.database.models.transaction.TransactionEntity
import com.example.database.room.dao.DiseaseDao
import com.example.database.room.dao.DonationRequestDao
import com.example.database.room.dao.MedicineDao
import com.example.database.room.dao.TransactionDao
import com.example.database.room.typeConverters.DateTypeConverter
import com.example.database.room.typeConverters.StringListTypeConverter

@Database(
    entities = [
        DiseaseEntity::class, MedicineEntity::class, SymptomEntity::class,
        TransactionEntity::class, DonationRequestEntity::class, DiseaseMedicineCrossRef::class
    ],
    version = 1
)
@TypeConverters(
    StringListTypeConverter::class,
    DateTypeConverter::class
)
abstract class MedicalServiceDatabase : RoomDatabase() {

    abstract fun getDiseaseDao(): DiseaseDao

    abstract fun getMedicineDao(): MedicineDao

    abstract fun getDonationRequestDao(): DonationRequestDao

    abstract fun getTransactionDao(): TransactionDao
}