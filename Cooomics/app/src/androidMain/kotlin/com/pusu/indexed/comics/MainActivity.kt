package com.pusu.indexed.comics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pusu.indexed.comics.di.appModule
import com.pusu.indexed.comics.navigation.AppNavigation
import com.pusu.indexed.comics.platform.createHttpClient
import org.koin.core.context.startKoin

/**
 * 主 Activity
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用平台特定的 HttpClient 工厂创建客户端
        val httpClient = createHttpClient()

        // 初始化 Koin
        startKoin {
            modules(appModule(httpClient))
        }

        setContent {
            AppNavigation()
        }
    }
}
