import java.util.Properties

plugins {
    id("mohamed.android.application")
    id("mohamed.android.application.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.medicalservice"

    defaultConfig {
        applicationId = "com.example.medicalservice"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val prop = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        manifestPlaceholders["GOOGLE_MAPS_API_KEY"] = prop.getProperty("GOOGLE_MAPS_API_KEY")

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        resources.excludes += "META-INF/atomicfu.kotlin_module"
    }

    sourceSets.getByName("main") {
        java.srcDirs("../core/domain/build/generated/ksp/debug/kotlin")
        java.srcDirs("../core/data/build/generated/ksp/debug/kotlin")
        java.srcDirs("../core/database/build/generated/ksp/debug/kotlin")
        java.srcDirs("../external/mapLocation/build/generated/ksp/debug/kotlin")
        java.srcDirs("../external/auth/build/generated/ksp/debug/kotlin")
        java.srcDirs("build/generated/ksp/debug/kotlin")
    }

    kotlinOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.kt)
    implementation(libs.coil.gif)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.workmanager)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.androidx.paging.testing)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    implementation(libs.google.play.services.text.recognition)

    implementation(project(":external:auth"))
    implementation(project(":external:mapLocation"))
    implementation(project(":common"))
    implementation(project(":external:ComposeComponents"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:worker"))
}

fun getCoreModuleSourceSet(name: String) = "../core/$name/build/generated/ksp/debug/kotlin"