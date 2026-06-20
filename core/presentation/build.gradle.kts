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
    //Nav3
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)
    api(libs.androidx.lifecycle.viewmodel.navigation3)
    api(libs.androidx.navigationevent)

    //KotlinSerialization
    api(libs.kotlinx.serialization.json)
}
