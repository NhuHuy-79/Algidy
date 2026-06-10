plugins {
    id("algidy.android.library")
}

android {
    namespace = "com.nhuhuy.algidy.core.datastore"
}

dependencies {
    implementation(project(":core:model"))
    api(libs.androidx.datastore.preferences)
    implementation(libs.koin.android)
}
