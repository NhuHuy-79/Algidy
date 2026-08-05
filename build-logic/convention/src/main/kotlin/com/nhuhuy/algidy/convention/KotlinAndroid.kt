package com.nhuhuy.algidy.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: Any,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val compileSdkValue = libs.findVersion("android-compileSdk").get().toString().toInt()
    val minSdkValue = libs.findVersion("android-minSdk").get().toString().toInt()
    val jvmTargetValue = libs.findVersion("android-jvmTarget").get().toString()

    when (commonExtension) {
        is LibraryExtension -> {
            commonExtension.compileSdk = compileSdkValue
            commonExtension.defaultConfig.minSdk = minSdkValue
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.toVersion(jvmTargetValue)
                targetCompatibility = JavaVersion.toVersion(jvmTargetValue)
            }
        }

        is ApplicationExtension -> {
            commonExtension.compileSdk = compileSdkValue
            commonExtension.defaultConfig.minSdk = minSdkValue
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.toVersion(jvmTargetValue)
                targetCompatibility = JavaVersion.toVersion(jvmTargetValue)
            }
        }

        is TestExtension -> {
            commonExtension.compileSdk = compileSdkValue
            commonExtension.defaultConfig.minSdk = minSdkValue
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.toVersion(jvmTargetValue)
                targetCompatibility = JavaVersion.toVersion(jvmTargetValue)
            }
        }

        is DynamicFeatureExtension -> {
            commonExtension.compileSdk = compileSdkValue
            commonExtension.defaultConfig.minSdk = minSdkValue
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.toVersion(jvmTargetValue)
                targetCompatibility = JavaVersion.toVersion(jvmTargetValue)
            }
        }
    }

    configureKotlin(jvmTargetValue)
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
@Suppress("unused")
internal fun Project.configureKotlinJvm() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val jvmTargetValue = libs.findVersion("android-jvmTarget").get().toString()

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.toVersion(jvmTargetValue)
        targetCompatibility = JavaVersion.toVersion(jvmTargetValue)
    }

    configureKotlin(jvmTargetValue)
}

/**
 * Configures Kotlin options
 */
private fun Project.configureKotlin(jvmTargetValue: String) {
    // Use withType to find all KotlinCompile tasks and configure them
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(jvmTargetValue))
        }
    }
}
