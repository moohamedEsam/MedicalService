package com.example.medicalservice

import android.os.Build
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.common.navigation.NavigationIntent
import com.example.domain.usecase.sync.OneTimeSyncWorkUseCase
import com.example.functions.snackbar.BaseSnackBarManager
import com.example.functions.snackbar.SnackBarManager
import com.example.worker.SyncWorker
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
@ComponentScan
class AppModule {

    context(Scope)
            @Single
    fun provideImageLoader() = ImageLoader.Builder(androidContext())
        .crossfade(true)
        .components {
            if (Build.VERSION.SDK_INT >= 28)
                add(ImageDecoderDecoder.Factory())
            else
                add(GifDecoder.Factory())
        }
        .build()

    @Factory
    fun provideCoroutineExceptionHandler() = CoroutineExceptionHandler { _, throwable ->
        Log.e("Error", "CoroutineExceptionHandler: ${throwable.message}")
        throwable.printStackTrace()
    }

    @Single([SnackBarManager::class])
    fun provideSnackBarManager() = BaseSnackBarManager()

    @Single
    fun provideNavigationChannel() = Channel<NavigationIntent>(
        capacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    context (Scope)
            @Factory
    fun provideOneTimeSyncUseCase() = OneTimeSyncWorkUseCase {
        val workRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(SyncWorker.workConstraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(SyncWorker.workName)
            .build()

        val workManager = WorkManager.getInstance(androidApplication())
        workManager.enqueue(workRequest)
        workManager.getWorkInfoByIdLiveData(workRequest.id)
    }
}

