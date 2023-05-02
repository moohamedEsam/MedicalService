import java.util.Properties
plugins {
    id("mohamed.android.application")
    id("mohamed.android.application.compose")
    alias(libs.plugins.ksp) apply true
    alias(libs.plugins.kotlin.serialization) apply true
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

    sourceSets.getByName("main"){
        java.srcDirs("../auth/build/generated/ksp/debug/kotlin")
        java.srcDirs("../mapLocation/build/generated/ksp/debug/kotlin")
        java.srcDirs("build/generated/ksp/debug/kotlin")
    }

    kotlinOptions{
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
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.google.play.services.text.recognition)

    implementation(project(":external:auth"))
    implementation(project(":external:mapLocation"))
    implementation(project(":common"))
    implementation(project(":external:ComposeComponents"))
    implementation(project(":model"))
}