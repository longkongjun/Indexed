rootProject.name = "Cooomics"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

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
include(":shared:feature:search")

// Navigation
include(":shared:navigation")

include(":composeApp")