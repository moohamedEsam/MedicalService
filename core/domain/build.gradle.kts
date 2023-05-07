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
    implementation(project(":core:data"))
}