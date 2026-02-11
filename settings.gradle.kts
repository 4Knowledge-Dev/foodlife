pluginManagement {
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
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FoodLife"
include(":app")
include(":core")
include(":core:ui")
include(":feature")
include(":feature:authentication")
include(":core:model")
include(":core:domain")
include(":core:common")
include(":feature:onboarding")
include(":core:data")
include(":feature:nutrition")
include(":feature:planner")
include(":feature:explore")
include(":core:api")
include(":feature:recipe")
