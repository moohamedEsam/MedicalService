plugins {
    id("mohamed.android.library")
    alias(libs.plugins.kotlin.serialization) apply true
}

android {
    namespace = "com.example.domain"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":core:data"))
}