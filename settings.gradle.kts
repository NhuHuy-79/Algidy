pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Algidy"
include(":app")

// Core modules
include(":core:model")
include(":core:designsystem")
include(":core:database")
include(":core:data")
include(":core:common")
include(":core:network")
include(":core:presentation")
include(":core:datastore")
include(":core:notifications")

// Feature modules
include(":feature:inventory")
include(":feature:scanner")
include(":feature:analytics")
include(":feature:settings")
include(":feature:food-entry")
