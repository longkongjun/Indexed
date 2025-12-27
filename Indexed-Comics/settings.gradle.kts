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
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Coooomics"

// Shared KMM + Compose Multiplatform modules
// Core modules
include(":shared:core:model")
include(":shared:core:utils")
include(":shared:core:ui")
include(":shared:core:network")

// Domain modules
include(":shared:domain:feed")
include(":shared:domain:discover")

// Data modules
include(":shared:data:jikan")

// Feature modules
include(":shared:feature:discover")
include(":shared:feature:anime-detail")

// Navigation
include(":shared:navigation")

// Platform modules (集中到 apps/)
include(":apps:android")
include(":apps:desktop")
include(":apps:web")
include(":apps:ios")
