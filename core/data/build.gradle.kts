plugins {
    id("algidy.android.library")
}

android {
    namespace = "com.nhuhuy.algidy.core.data"
}

dependencies {
    api(libs.timber)
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.play.services)

    /* //File Kit
    api(libs.filekit.core)
    api(libs.filekit.dialogs.compose)

}*/
}