plugins {
    id("algidy.android.feature")
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.nhuhuy.algidy.feature.settings"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:notifications"))
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
}
