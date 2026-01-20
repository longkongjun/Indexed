package com.pusu.indexed.comics

import androidx.compose.runtime.*
import androidx.compose.ui.window.ComposeUIViewController
import com.pusu.indexed.comics.di.appModule
import com.pusu.indexed.comics.di.koinInstance
import com.pusu.indexed.comics.navigation.AppNavigation
import com.pusu.indexed.comics.platform.createHttpClient
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.context.startKoin

/**
 * iOS 应用入口
 */
@Composable
fun IOSApp() {
    // 使用平台特定的 HttpClient 工厂创建客户端
    val httpClient = remember { createHttpClient() }
    
    // 初始化 Koin（只初始化一次）
    LaunchedEffect(httpClient) {
        val koinApp = startKoin {
            modules(appModule(httpClient))
        }
        koinInstance = koinApp.koin
    }
    
    AppNavigation()
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
