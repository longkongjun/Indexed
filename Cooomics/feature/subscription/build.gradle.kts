plugins {
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
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
                implementation(compose.materialIconsExtended)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
            }
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
    jvmToolchain(11)
}

android {
    namespace = "com.pusu.indexed.shared.subscription"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
