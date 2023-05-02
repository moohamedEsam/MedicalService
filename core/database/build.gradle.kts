plugins {
    id("mohamed.android.library")
}

android {
    namespace = "com.example.database"
}

dependencies {
    implementation(project(":common"))
}