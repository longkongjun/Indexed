package com.pusu.indexed.comics

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.ComposeUIViewController
import com.pusu.indexed.comics.di.DependencyContainer
import com.pusu.indexed.comics.navigation.AppNavigation
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

/**
 * iOS 应用入口
 */
@Composable
fun IOSApp() {
    // 创建 HttpClient
    val httpClient = remember {
        HttpClient(Darwin) {
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
    }
    
    // 创建依赖容器
    val dependencyContainer = remember { DependencyContainer(httpClient) }
    val scope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
    
    MaterialTheme {
        AppNavigation(
            dependencyContainer = dependencyContainer,
            scope = scope
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
fun MainViewController() = ComposeUIViewController(
    configure = {
        // 禁用帧率限制检查，允许使用高刷新率
        enforceStrictPlistSanityCheck = false
    }
) { 
    IOSApp() 
}
