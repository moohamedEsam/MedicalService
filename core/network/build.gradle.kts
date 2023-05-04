plugins {
    id("mohamed.android.library")
    alias(libs.plugins.kotlin.serialization) apply true
}

android {
    namespace = "com.example.network"
}

dependencies {
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
}