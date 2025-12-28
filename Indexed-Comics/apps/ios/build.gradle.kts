plugins {
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    // iOS 目标平台
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:ui"))
            implementation(project(":shared:feature:discover"))
            implementation(project(":shared:feature:anime-detail"))
            implementation(project(":shared:domain:discover"))
            implementation(project(":shared:domain:feed"))
            implementation(project(":shared:data:jikan"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            
            // Ktor for iOS
            implementation(libs.ktor.client.darwin)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

