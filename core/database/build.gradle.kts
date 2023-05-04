plugins {
    id("mohamed.android.library")
    id("mohamed.android.room")
    alias(libs.plugins.kotlin.serialization) apply true
}

android {
    namespace = "com.example.database"
    kotlinOptions{
        freeCompilerArgs = listOf("-Xcontext-receivers")
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies{
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.serialization.json)
}