plugins {
    id("mohamed.android.library")
}

android {
    namespace = "com.example.worker"
}

dependencies {
    implementation(libs.androidx.work.ktx)
    implementation(libs.koin.workmanager)
    implementation(project((":core:data")))
}