plugins {
    id("mohamed.android.library")
}

android {
    namespace = "com.example.workmanager"
}

dependencies {
    implementation(project(":common"))
}