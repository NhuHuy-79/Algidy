plugins {
    id("algidy.android.library.compose")
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.nhuhuy.algidy.core.presentation"

}


dependencies {
    api(project(":core:model"))
    api(project(":core:designsystem"))
    api(project(":core:common"))

    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(libs.koin.compose)
    api(libs.accompanist.permissions)

    //Immutable Collection
    api(libs.kotlinx.collections.immutable)
    api(libs.androidx.navigation3.runtime)
    api(libs.kotlinx.serialization.json)
}
