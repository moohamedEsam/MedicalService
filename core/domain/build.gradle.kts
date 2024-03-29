plugins {
    id("mohamed.android.library")
    alias(libs.plugins.kotlin.serialization) apply true
}

android {
    namespace = "com.example.domain"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.dataStore.core)
    implementation(libs.google.play.services.text.recognition)
    implementation(project(":core:data"))
    implementation(project(":core:dataStore"))
}