plugins {
    id("algidy.android.library.compose")
}

android {
    namespace = "com.nhuhuy.algidy.core.designsystem"
}

dependencies {
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.android)
}
