plugins {
    id("mohamed.android.library")
}

android {
    namespace = "com.example.data"
}

dependencies {
    implementation(project(":common"))
}