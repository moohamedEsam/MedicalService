package com.example.database

import androidx.room.Room
import com.example.database.room.MedicalServiceDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
class DatabaseModule {

    context(Scope)
    @Single
    fun provideDatabase() = Room.databaseBuilder(
        context = androidContext(),
        klass = MedicalServiceDatabase::class.java,
        name = "medical_service_database"
    ).build()

    @Single
    fun provideDiseaseDao(database: MedicalServiceDatabase) = database.getDiseaseDao()

    @Single
    fun provideMedicineDao(database: MedicalServiceDatabase) = database.getMedicineDao()

    @Single
    fun provideDonationRequestDao(database: MedicalServiceDatabase) = database.getDonationRequestDao()

    @Single
    fun provideTransactionDao(database: MedicalServiceDatabase) = database.getTransactionDao()
}