plugins {
    id("einvoice.android.feature")
    id("einvoice.android.test")
}

android {
    namespace = "com.example.auth"
}


dependencies {
    implementation(libs.androidx.activity.compose)
}