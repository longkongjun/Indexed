package com.pusu.indexed.comics

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pusu.indexed.comics.di.appModule
import com.pusu.indexed.comics.navigation.AppNavigation
import com.pusu.indexed.comics.platform.createHttpClient
import org.koin.core.context.startKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Cooomics",
    ) {
        // 使用平台特定的 HttpClient 工厂创建客户端
        val httpClient = remember { createHttpClient() }

        // 初始化 Koin
        remember {
            startKoin {
                modules(appModule(httpClient))
            }
        }

        MaterialTheme {
            AppNavigation()
        }
    }
}
