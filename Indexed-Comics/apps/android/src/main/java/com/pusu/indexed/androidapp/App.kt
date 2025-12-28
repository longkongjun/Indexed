package com.pusu.indexed.androidapp

import android.app.Application
import com.pusu.indexed.androidapp.di.DependencyContainer
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Android Application 类
 * 
 * 在这里初始化依赖注入容器
 */
class App : Application() {
    
    /**
     * 全局的依赖注入容器
     */
    lateinit var dependencyContainer: DependencyContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        // 1. 创建 Android 平台的 HttpClient
        val httpClient = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
            
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        android.util.Log.d("HTTP_CLIENT", message)
                    }
                }
                level = LogLevel.ALL  // 显示所有日志：请求头、请求体、响应头、响应体
            }
        }
        
        // 2. 创建依赖注入容器
        dependencyContainer = DependencyContainer(httpClient)
        
        // 保存全局引用
        instance = this
    }
    
    companion object {
        lateinit var instance: App
            private set
    }
}

