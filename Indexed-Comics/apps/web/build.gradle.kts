plugins {
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig { outputFileName = "webApp.js" }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared:core:ui"))
                implementation(project(":shared:feature:discover"))
                implementation(project(":shared:feature:anime-detail"))
                implementation(project(":shared:domain:discover"))
                implementation(project(":shared:data:jikan"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                
                // Ktor for web
                implementation(libs.ktor.client.js)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
            }
        }
        val jsMain by getting {
            dependencies { implementation(compose.html.core) }
        }
        val commonTest by getting { dependencies { implementation(kotlin("test")) } }
    }
}
