plugins {
    id("algidy.android.library")
}

android {
    namespace = "com.nhuhuy.algidy.core.data"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:ai"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)
}
