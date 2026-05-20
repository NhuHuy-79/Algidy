plugins {
    id("algidy.android.library.compose")
}

android {
    namespace = "com.nhuhuy.algidy.core.designsystem"
}

dependencies {
    api(libs.androidx.material3)
    api(libs.androidx.material3.android)
    api(libs.androidx.material.icons.extended)
    api(libs.coil.compose)

    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.0")
}
