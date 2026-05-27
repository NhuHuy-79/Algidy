plugins {
    id("algidy.android.feature")
}

android {
    namespace = "com.nhuhuy.algidy.feature.scanner"
}

dependencies {
    implementation("androidx.camera:camera-core:1.5.0-alpha05")
    implementation("androidx.camera:camera-camera2:1.5.0-alpha05")
    implementation("androidx.camera:camera-lifecycle:1.5.0-alpha05")
    implementation("androidx.camera:camera-view:1.5.0-alpha05")
    implementation("androidx.camera:camera-extensions:1.5.0-alpha05")
    implementation("androidx.camera:camera-compose:1.5.0-alpha05")

    // --- ML Kit (Unbundled - Using Google Play Services to reduce APK size & 16KB support) ---
    // Nhận diện văn bản (OCR) - Rất cần để đọc ngày hết hạn
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")

    // Quét mã vạch (Barcode Scanning)
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.1")

    // --- Guava (Cần thiết để xử lý ListenableFuture của CameraX mượt hơn) ---
    implementation("com.google.guava:guava:33.0.0-android")

    implementation("com.google.mlkit:entity-extraction:16.0.0-beta6")
}
