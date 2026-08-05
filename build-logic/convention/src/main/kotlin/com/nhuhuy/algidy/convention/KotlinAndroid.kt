package com.nhuhuy.algidy.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: Any,
) {
    val compileSdkValue = 36
    val minSdkValue = 30

    when (commonExtension) {
        is LibraryExtension -> {
            commonExtension.compileSdk = compileSdkValue
            commonExtension.defaultConfig.minSdk = minSdkValue
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }

        is ApplicationExtension -> {
            commonExtension.compileSdk = compileSdkValue
            commonExtension.defaultConfig.minSdk = minSdkValue
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }

        is TestExtension -> {
            commonExtension.compileSdk = compileSdkValue
            commonExtension.defaultConfig.minSdk = minSdkValue
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }

    configureKotlin()
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
@Suppress("unused")
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    configureKotlin()
}

/**
 * Configures Kotlin options
 */
private fun Project.configureKotlin() {
    // Use withType to find all KotlinCompile tasks and configure them
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}
