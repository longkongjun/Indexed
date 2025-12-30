package com.pusu.indexed.comics

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pusu.indexed.comics.di.DependencyContainer
import com.pusu.indexed.comics.navigation.AppNavigation
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Cooomics",
    ) {
        // 创建 HttpClient
        val httpClient = remember {
            HttpClient(OkHttp) {
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
        val scope = rememberCoroutineScope { SupervisorJob() + Dispatchers.Main }

        MaterialTheme {
            AppNavigation(dependencyContainer, scope)
        }
    }
}
