import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.common"
    compileSdk = 33
    defaultConfig {
        minSdk = 23
        val prop = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        buildTypes {
            debug {
                buildConfigField("String", "SHARED_PREF_KEY", prop["SHARED_PREF_KEY"].toString())
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.androidx.security.crypto)
    implementation(libs.kotlinx.coroutines.android)
}