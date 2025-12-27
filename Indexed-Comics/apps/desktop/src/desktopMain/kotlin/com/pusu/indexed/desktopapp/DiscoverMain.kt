package com.pusu.indexed.desktopapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pusu.indexed.desktopapp.di.DependencyContainer
import com.pusu.indexed.shared.feature.discover.DiscoverScreen
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

/**
 * Desktop 应用的入口（Discover 功能）
 * 
 * 展示如何在 Desktop 平台使用依赖注入
 */
fun main() = application {
    // ========================================
    // 1. 创建 HttpClient (Desktop 使用 CIO 引擎)
    // ========================================
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
    
    // ========================================
    // 2. 创建依赖注入容器
    // ========================================
    val dependencyContainer = DependencyContainer(httpClient)
    
    // ========================================
    // 3. 创建 ViewModel
    // ========================================
    val viewModel = dependencyContainer.createDiscoverViewModel(
        coroutineScope = CoroutineScope(Dispatchers.Default)
    )
    
    // ========================================
    // 4. 创建窗口
    // ========================================
    Window(
        onCloseRequest = ::exitApplication,
        title = "Indexed Comics - Discover"
    ) {
        DiscoverScreen(
            viewModel = viewModel,
            onNavigateToDetail = { animeId ->
                println("Navigate to detail: $animeId")
            }
        )
    }
}

