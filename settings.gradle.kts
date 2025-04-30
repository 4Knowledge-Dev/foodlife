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
