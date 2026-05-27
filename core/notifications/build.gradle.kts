plugins {
    id("algidy.android.library")
}

android {
    namespace = "com.nhuhuy.algidy.core.notifications"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(libs.koin.androidx.workmanager)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.work.runtime.ktx)
}
