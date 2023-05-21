plugins {
    id("mohamed.android.library")
    id("mohamed.android.room")
    id("mohamed.android.test")
    alias(libs.plugins.kotlin.serialization) apply true
}

android {
    namespace = "com.example.database"
}

dependencies{
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.serialization.json)
    androidTestImplementation(libs.androidx.paging.testing)
}