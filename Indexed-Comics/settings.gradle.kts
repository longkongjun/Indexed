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
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Coooomics"

// Shared KMM + Compose Multiplatform modules
include(":shared:core:model")
include(":shared:core:utils")
include(":shared:core:ui")
include(":shared:core:network")
include(":shared:domain:feed")
include(":shared:data:jikan")
include(":shared:feature:discover")
include(":shared:feature:anime-detail")
include(":shared:navigation")

// Platform modules (集中到 apps/)
include(":apps:android")
include(":apps:desktop")
include(":apps:web")
include(":apps:ios")
