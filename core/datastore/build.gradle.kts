plugins {
    id("algidy.android.library")
}

android {
    namespace = "com.nhuhuy.algidy.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.koin.android)
}
