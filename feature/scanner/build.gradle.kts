plugins {
    id("algidy.android.feature")
}

android {
    namespace = "com.nhuhuy.algidy.feature.scanner"
}

dependencies {
    implementation("androidx.camera:camera-core:1.4.0")
    implementation("androidx.camera:camera-camera2:1.4.0")
    implementation("androidx.camera:camera-lifecycle:1.4.0")
    implementation("androidx.camera:camera-view:1.4.0")
    implementation("androidx.camera:camera-extensions:1.4.0")

    // --- ML Kit (Chọn các gói tùy theo nhu cầu của bạn) ---
    // Nhận diện văn bản (OCR) - Rất cần để đọc ngày hết hạn
    implementation("com.google.mlkit:text-recognition:16.0.1")

    // Quét mã vạch (Barcode Scanning)
    implementation("com.google.mlkit:barcode-scanning:17.3.0")

    // Phát hiện và theo dõi vật thể (Dùng để clone Scanner của Google Drive)
    implementation("com.google.mlkit:object-detection:17.0.2")

    // --- Guava (Cần thiết để xử lý ListenableFuture của CameraX mượt hơn) ---
    implementation("com.google.guava:guava:33.0.0-android")

    implementation("com.google.mlkit:image-labeling:17.0.9")
}
