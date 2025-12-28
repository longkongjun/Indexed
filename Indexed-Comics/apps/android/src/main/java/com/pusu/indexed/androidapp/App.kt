package com.pusu.indexed.androidapp

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import com.pusu.indexed.androidapp.di.DependencyContainer
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import java.io.File

/**
 * Android Application 类
 * 
 * 在这里初始化依赖注入容器和 Coil 图片加载器
 */
class App : Application(), SingletonImageLoader.Factory {
    
    /**
     * 全局的依赖注入容器
     */
    lateinit var dependencyContainer: DependencyContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        // 0. 配置 Coil 图片加载器（会自动使用此配置）
        // SingletonImageLoader.Factory 接口会在首次使用 AsyncImage 时调用 newImageLoader()
        
        // 1. 创建 Android 平台的 HttpClient（带 HTTP 缓存）
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
        
        // 2. 创建依赖注入容器
        dependencyContainer = DependencyContainer(httpClient)
        
        // 保存全局引用
        instance = this
    }
    
    /**
     * 创建和配置 Coil ImageLoader
     * 
     * 优化配置：
     * - 内存缓存：25% 可用内存，强引用缓存
     * - 磁盘缓存：250MB，存储在 app cache 目录
     * 
     * 缓存位置：/data/data/com.pusu.indexed.comics/cache/image_cache/
     */
    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            // ========== 内存缓存配置 ==========
            .memoryCache {
                MemoryCache.Builder()
                    // 设置最大缓存大小为可用内存的 25%
                    .maxSizePercent(context, percent = 0.25)
                    .build()
            }
            // ========== 磁盘缓存配置 ==========
            .diskCache {
                DiskCache.Builder()
                    // 缓存目录（使用 okio.Path）
                    .directory(context.cacheDir.resolve("image_cache").toOkioPath())
                    // 最大缓存 250MB
                    .maxSizeBytes(250L * 1024 * 1024)
                    .build()
            }
            .build()
    }
    
    companion object {
        lateinit var instance: App
            private set
    }
}

