plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.pusu.indexed.androidapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pusu.indexed.androidapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    flavorDimensions += "app"
    productFlavors {
        create("demo") {
            dimension = "app"
            applicationId = "com.pusu.indexed.comics"
            namespace = "com.pusu.indexed.comics"
        }
        create("shelf") {
            dimension = "app"
            applicationId = "com.pusu.indexed.shelf"
            namespace = "com.pusu.indexed.shelf"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Shared modules
    implementation(project(":shared:core:ui"))
    implementation(project(":shared:feature:discover"))
    implementation(project(":shared:feature:anime-detail"))
    implementation(project(":shared:domain:discover"))
    implementation(project(":shared:data:jikan"))

    // Ktor (用于 DI 容器中创建 HttpClient)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    // Android
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
}


kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
