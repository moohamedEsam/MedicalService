package com.example.medicalservice

import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.auth.domain.LoginUseCase
import com.example.auth.domain.RegisterUseCase
import com.example.common.models.Result
import com.example.functions.snackbar.BaseSnackBarManager
import com.example.functions.snackbar.SnackBarManager
import kotlinx.coroutines.delay
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.module

val appModule = module {
    single { provideImageLoader() }
    single<SnackBarManager> { BaseSnackBarManager() }
    factory { LoginUseCase { Result.Success(Unit) } }
    factory { RegisterUseCase { Result.Success(Unit) } }
}


fun Scope.provideImageLoader() = ImageLoader.Builder(androidContext())
    .crossfade(true)
    .components {
        if (Build.VERSION.SDK_INT >= 28)
            add(ImageDecoderDecoder.Factory())
        else
            add(GifDecoder.Factory())
    }
    .build()