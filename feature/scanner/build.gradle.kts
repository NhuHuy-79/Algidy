plugins {
    id("algidy.android.feature")
}

android {
    namespace = "com.nhuhuy.algidy.feature.scanner"
}

dependencies {
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions1)
    implementation(libs.androidx.camera.compose)
    implementation(libs.play.services.mlkit.barcode.scanning)
    implementation(libs.guava)

    implementation(project(":feature:food-entry"))
}
