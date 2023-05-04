package com.example.medicalservice

import android.os.Build
import android.util.Log
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.functions.snackbar.BaseSnackBarManager
import com.example.functions.snackbar.SnackBarManager
import kotlinx.coroutines.CoroutineExceptionHandler
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
}

