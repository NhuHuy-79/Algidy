import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            pluginManager.apply {
                apply("algidy.android.library.compose")
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

            dependencies {
                add("implementation", project(":core:presentation"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:database"))

                // ViewModel and Lifecycle
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add(
                    "implementation",
                    libs.findLibrary("androidx-lifecycle-viewmodel-compose").get()
                )

                // Koin for DI
                add("implementation", libs.findLibrary("koin-android").get())
                add("implementation", libs.findLibrary("koin-compose").get())
                add("implementation", libs.findLibrary("koin-annotations").get())
                add("ksp", libs.findLibrary("koin-ksp-compiler").get())

                // Common test dependencies
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("koin-test").get())
                add("testImplementation", libs.findLibrary("robolectric").get())
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("turbine").get())

                // Android test dependencies
                add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
                add("androidTestImplementation", libs.findLibrary("androidx-ui-test-junit4").get())
                add("androidTestImplementation", libs.findLibrary("mockk-android").get())
            }
        }
    }
}
