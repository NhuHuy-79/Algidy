import com.android.build.api.dsl.ApplicationExtension
import com.nhuhuy.algidy.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import java.io.FileInputStream
import java.util.Properties

@Suppress("unused")
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk =
                    libs.findVersion("android-targetSdk").get().toString().toInt()

                testOptions.animationsDisabled = true

                buildFeatures {
                    compose = true
                    buildConfig = true
                }

                val keystorePropertiesFile = rootProject.file("keystore.properties")
                val keystoreProperties = Properties()
                if (keystorePropertiesFile.exists()) {
                    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                }

                signingConfigs {
                    create("release") {
                        val propertiesExist = keystoreProperties.isNotEmpty()
                        if (propertiesExist) {
                            storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
                            storePassword = keystoreProperties["storePassword"] as String
                            keyAlias = keystoreProperties["keyAlias"] as String
                            keyPassword = keystoreProperties["keyPassword"] as String
                        }
                    }
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "new-proguard-rules.pro"
                        )
                        if (keystoreProperties.isNotEmpty()) {
                            signingConfig = signingConfigs.getByName("release")
                        }
                    }
                }

                lint {
                    abortOnError = false
                    checkDependencies = true
                    ignoreWarnings = false
                    xmlReport = true
                    htmlReport = true
                    lintConfig = file("${project.rootDir}/lint.xml")
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        excludes += "META-INF/LICENSE.md"
                        excludes += "META-INF/LICENSE-notice.md"
                        excludes += "META-INF/LICENSE.txt"
                        excludes += "META-INF/NOTICE.md"
                        excludes += "META-INF/NOTICE.txt"
                        excludes += "META-INF/ASL2.0"
                        excludes += "META-INF/LICENSE"
                        excludes += "META-INF/NOTICE"
                    }
                }
            }

            dependencies {
                add("testImplementation", kotlin("test"))
            }
        }
    }
}
