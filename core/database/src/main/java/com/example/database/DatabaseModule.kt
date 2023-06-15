package com.example.database

import androidx.room.Room
import com.example.database.room.ClearDatabaseUseCase
import com.example.database.room.MedicalServiceDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.Factory
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
    fun provideDonationRequestDao(database: MedicalServiceDatabase) =
        database.getDonationRequestDao()

    @Single
    fun provideTransactionDao(database: MedicalServiceDatabase) = database.getTransactionDao()

    @Single
    fun provideUserDao(database: MedicalServiceDatabase) = database.getUserDao()

    @Single
    fun provideDiagnosisRequestDao(database: MedicalServiceDatabase) =
        database.getDiagnosisRequestDao()

    @Single
    fun provideDiagnosisResultDao(database: MedicalServiceDatabase) =
        database.getDiagnosisResultDao()

    @Factory
    fun provideClearDatabaseUseCase(
        database: MedicalServiceDatabase
    ) = ClearDatabaseUseCase {
        withContext(Dispatchers.IO) {
            database.clearAllTables()
        }
    }
}