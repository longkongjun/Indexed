package com.pusu.indexed.comics

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import okio.Path.Companion.toOkioPath

/**
 * Android Application 类
 * 
 * 在这里初始化 Coil 图片加载器
 */
class App : Application(), SingletonImageLoader.Factory {
    
    override fun onCreate() {
        super.onCreate()
        
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

