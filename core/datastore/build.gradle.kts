plugins {
    id("algidy.android.library")
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.nhuhuy.algidy.core.datastore"
}

dependencies {
    implementation(project(":core:model"))
    api(libs.androidx.datastore.preferences)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.serialization.json)
}
