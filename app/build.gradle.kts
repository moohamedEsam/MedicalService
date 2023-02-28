import java.util.Properties
plugins {
    id("einvoice.android.application")
    id("einvoice.android.application.compose")
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

    implementation(project(":auth"))
    implementation(project(":mapLocation"))
    implementation(project(":common"))
    implementation(project(":ComposeComponents"))
}