plugins {
    id("algidy.android.library")
}

android {
    namespace = "com.nhuhuy.algidy.core.domain"
}

dependencies {
    // Bây giờ bạn có thể sử dụng bản android
    implementation(libs.kotlinx.coroutines.android)
    implementation(project(":core:model"))
}