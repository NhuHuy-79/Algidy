plugins {
    id("algidy.android.library")
}

android {
    namespace = "com.nhuhuy.algidy.core.ai"
}

dependencies {
    implementation(libs.google.ai.client)
}
