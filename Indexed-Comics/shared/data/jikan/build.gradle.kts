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
                implementation(project(":shared:core:network"))
                implementation(project(":shared:domain:discover"))
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting { dependencies { implementation(kotlin("test")) } }
    }
    jvmToolchain(17)
}

android {
    namespace = "com.pusu.indexed.jikan"
    compileSdk = 36
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
