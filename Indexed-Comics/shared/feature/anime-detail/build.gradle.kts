plugins {
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidTarget()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    jvm("desktop")
    js(IR) { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(project(":shared:core:model"))
                implementation(project(":shared:core:ui"))
                implementation(project(":shared:domain:feed"))
                implementation(project(":shared:domain:discover"))
                implementation(project(":shared:data:jikan"))
                
                // Coil Image Loading
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
            }
        }
        val commonTest by getting {
            dependencies { implementation(kotlin("test")) }
        }
        val androidMain by getting {
            dependencies { implementation(libs.androidx.activity.compose) }
        }
        val desktopMain by getting {
            dependencies { implementation(compose.desktop.currentOs) }
        }
        val jsMain by getting {
            dependencies { implementation(compose.html.core) }
        }
    }
    jvmToolchain(17)
}

android {
    namespace = "com.pusu.indexed.shared.anime_detail"
    compileSdk = 36
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
