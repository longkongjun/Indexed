plugins {
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Shared modules
                implementation(project(":shared:core:ui"))
                implementation(project(":shared:feature:discover"))
                implementation(project(":shared:feature:anime-detail"))
                implementation(project(":shared:domain:discover"))
                implementation(project(":shared:data:jikan"))
                
                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                
                // Ktor (用于 DI 容器中创建 HttpClient)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
    jvmToolchain(17)
}

compose.desktop.application {
    mainClass = "com.pusu.indexed.desktopapp.MainKt"
}
