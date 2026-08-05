import com.android.build.api.dsl.LibraryExtension
import com.nhuhuy.algidy.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

@Suppress("unused")
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                defaultConfig.consumerProguardFiles("consumer-rules.pro")
                
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
                add("implementation", libs.findLibrary("androidx-core-ktx").get())

                // Common test dependencies
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("turbine").get())
                add("testImplementation", libs.findLibrary("robolectric").get())
            }
        }
    }
}
