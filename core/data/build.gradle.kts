plugins {
    id("mohamed.android.library")
    id("mohamed.android.test")
}

android {
    namespace = "com.example.data"
}

dependencies{
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.room.runtime)
}