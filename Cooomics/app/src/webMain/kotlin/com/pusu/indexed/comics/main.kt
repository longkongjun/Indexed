package com.pusu.indexed.comics

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.pusu.indexed.comics.di.appModule
import com.pusu.indexed.comics.di.koinInstance
import com.pusu.indexed.comics.navigation.AppNavigation
import com.pusu.indexed.comics.platform.createHttpClient
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        // 使用平台特定的 HttpClient 工厂创建客户端
        val httpClient = remember { createHttpClient() }

        // 初始化 Koin
        remember {
            val koinApp = startKoin {
                modules(appModule(httpClient))
            }
            koinInstance = koinApp.koin
        }

        AppNavigation()
    }
}
