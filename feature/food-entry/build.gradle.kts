plugins {
    id("algidy.android.feature")
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.nhuhuy.algidy.feature.food_entry"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":core:datastore"))
}
