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
    implementation(project(":core:domain"))
    implementation(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.play.services)

    //Filkit
    api("io.github.vinceglb:filekit-core:0.15.0")
    api("io.github.vinceglb:filekit-dialogs-compose:0.15.0")


}