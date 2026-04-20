plugins {
    id("algidy.android.library")
}

android {
    namespace = "com.nhuhuy.algidy.core.data"
}

dependencies {
    api(libs.timber)
    implementation(project(":core:database"))
    implementation(project(":core:ai"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.android)
}
