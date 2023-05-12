plugins {
    id("mohamed.android.library")
    alias(libs.plugins.kotlin.serialization) apply true
}

android {
    namespace = "com.example.datastore"
}

dependencies {
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.kotlinx.serialization.json)
}


