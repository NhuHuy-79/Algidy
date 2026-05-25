import com.android.build.api.dsl.ApplicationExtension
import com.nhuhuy.algidy.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

@Suppress("unused")
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36

                testOptions.animationsDisabled = true

                buildFeatures {
                    compose = true
                }

                lint {
                    abortOnError = false
                    checkDependencies = true
                    ignoreWarnings = false
                    xmlReport = true
                    htmlReport = true
                    lintConfig = file("${project.rootDir}/lint.xml")
                }
            }

            dependencies {
                add("testImplementation", kotlin("test"))
                add("implementation", libs.findLibrary("logger").get())
            }
        }
    }
}
