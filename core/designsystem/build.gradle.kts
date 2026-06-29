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
    api(libs.coil.network.okhttp)

    implementation(libs.androidx.compose.ui.text.google.fonts)
}
