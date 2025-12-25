plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
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
                implementation(project(":shared:core:model"))
                api(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
            }
        }
        val commonTest by getting { dependencies { implementation(kotlin("test")) } }
        val androidMain by getting { dependencies { implementation(libs.ktor.client.okhttp) } }
        val desktopMain by getting { dependencies { implementation(libs.ktor.client.okhttp) } }

        val iosMain by creating {
            dependencies { implementation(libs.ktor.client.darwin) }
        }
        val iosArm64Main by getting
        val iosX64Main by getting
        val iosSimulatorArm64Main by getting
        iosArm64Main.dependsOn(iosMain)
        iosX64Main.dependsOn(iosMain)
        iosSimulatorArm64Main.dependsOn(iosMain)

        val jsMain by getting { dependencies { implementation(libs.ktor.client.js) } }
    }
    jvmToolchain(11)
}

android {
    namespace = "com.pusu.indexed.core.network"
    compileSdk = 36
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
