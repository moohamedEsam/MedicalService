plugins {
    id("mohamed.android.feature")
    id("mohamed.android.test")
}

android {
    namespace = "com.example.auth"
}


dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(project(":external:mapLocation"))
}