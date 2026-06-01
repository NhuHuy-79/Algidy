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

    // --- ML Kit (Unbundled - Using Google Play Services to reduce APK size & 16KB support) ---
    // Nhận diện văn bản (OCR) - Rất cần để đọc ngày hết hạn
    implementation(libs.play.services.mlkit.text.recognition)

    // Quét mã vạch (Barcode Scanning)
    implementation(libs.play.services.mlkit.barcode.scanning)

    // --- Guava (Cần thiết để xử lý ListenableFuture của CameraX mượt hơn) ---
    implementation(libs.guava)

    implementation(libs.entity.extraction)
}
