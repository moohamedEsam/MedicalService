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
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.coroutines.android)

}