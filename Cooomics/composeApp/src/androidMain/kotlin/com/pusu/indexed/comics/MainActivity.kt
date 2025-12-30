package com.pusu.indexed.comics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.pusu.indexed.comics.di.DependencyContainer
import com.pusu.indexed.comics.navigation.AppNavigation
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

/**
 * 主 Activity
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建 Android 平台的 HttpClient（带 HTTP 缓存）
        val httpClient = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
            
            // ========== 日志 ==========
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        android.util.Log.d("HTTP_CLIENT", message)
                    }
                }
                level = LogLevel.ALL  // 显示所有日志：请求头、请求体、响应头、响应体
            }
        }
        
        // 创建依赖注入容器
        val dependencyContainer = DependencyContainer(httpClient)
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        
        setContent {
            MaterialTheme {
                AppNavigation(
                    dependencyContainer = dependencyContainer,
                    scope = scope
                )
            }
        }
    }
}
