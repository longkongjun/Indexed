plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
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
                // utils 只包含纯工具函数，不依赖任何业务模块
            }
        }
        val commonTest by getting {
            dependencies { implementation(kotlin("test")) }
        }
    }
    jvmToolchain(17)
}

android {
    namespace = "com.pusu.indexed.shared.utils"
    compileSdk = 36
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
