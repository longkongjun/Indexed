package com.pusu.indexed.webapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.pusu.indexed.shared.feature.discover.DiscoverScreen
import com.pusu.indexed.webapp.di.DependencyContainer
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.document
import kotlinx.serialization.json.Json

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // 创建 HttpClient for Web
    val httpClient = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
    
    // 创建依赖注入容器
    val dependencyContainer = DependencyContainer(httpClient)
    
    // 启动应用
    ComposeViewport(document.body!!) {
        MaterialTheme {
            val scope = rememberCoroutineScope()
            val viewModel = remember {
                dependencyContainer.createDiscoverViewModel(scope)
            }
            
            DiscoverScreen(
                viewModel = viewModel,
                onNavigateToDetail = { animeId ->
                    console.log("Navigate to anime detail: $animeId")
                }
            )
        }
    }
}
