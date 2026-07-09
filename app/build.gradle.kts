plugins {
    id("algidy.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.aboutlibraries)
}

android {
    flavorDimensions += "store"

    productFlavors {
        create("play") {
            dimension = "store"
            buildConfigField("boolean", "IS_FOSS", "false")
        }

        create("foss") {
            dimension = "store"
            buildConfigField("boolean", "IS_FOSS", "true")
        }
    }

    namespace = "com.nhuhuy.algidy"
    defaultConfig {
        applicationId = "com.nhuhuy.algidy"
        targetSdk = 36
        versionCode = 4
        versionName = "1.3.0"
    }

    buildFeatures {
        buildConfig = true
    }


    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val fileName = "Algidy_${variant.name}_v${variant.versionName}.apk"
            output.outputFileName = fileName
        }
    }
}

dependencies {
    implementation(project(":core:presentation"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:notifications"))
    implementation(project(":core:datastore"))

    implementation(project(":feature:inventory"))
    implementation(project(":feature:scanner"))
    implementation(project(":feature:analytics"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:food-entry"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Koin & Navigation
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.androidx.workmanager)
    implementation(libs.androidx.navigation.compose)

    //Biometric Authenticator
    implementation(libs.androidx.biometric)

    //Glance
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.glance.preview)
    implementation(libs.androidx.glance.appwidget.preview)

    //Serialization
    implementation(libs.kotlinx.serialization.core)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
