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

// ============================================
// Core Modules (基础设施层)
// ============================================
include(":core:utils")
include(":core:network")

// ============================================
// Data Modules (数据层)
// ============================================
include(":data:jikan")

// ============================================
// Domain Modules (业务逻辑层)
// ============================================
include(":domain:anime")

// ============================================
// Feature Modules (功能模块层)
// ============================================
include(":feature:discover")
include(":feature:anime-detail")
include(":feature:search")
include(":feature:subscription")
include(":feature:settings")

// ============================================
// Application Module
// ============================================
include(":app")