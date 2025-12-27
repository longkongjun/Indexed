package com.pusu.indexed.androidapp.di

import com.pusu.indexed.data.jikan.mapper.JikanToDiscoverMapper
import com.pusu.indexed.data.jikan.repository.JikanDiscoverRepository
import com.pusu.indexed.domain.discover.repository.DiscoverRepository
import com.pusu.indexed.domain.discover.usecase.GetTrendingAnimeUseCase
import com.pusu.indexed.jikan.JikanApi
import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.createJikanApi
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverViewModel
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope

/**
 * Android 应用的依赖注入容器
 * 
 * 负责创建和管理所有依赖关系。
 * 这是一个简单的手动 DI 实现，不依赖第三方框架。
 * 
 * 依赖链：
 * HttpClient → JikanApi → Repository → UseCase → ViewModel
 */
class DependencyContainer(
    httpClient: HttpClient
) {
    // ========================================
    // Core 层：网络配置
    // ========================================
    
    private val httpClient: HttpClient = httpClient
    
    // ========================================
    // Data 层：API 和 Repository
    // ========================================
    
    /**
     * Jikan 客户端（封装了 HttpClient）
     */
    private val jikanClient: JikanClient by lazy {
        JikanClient(
            baseUrl = "https://api.jikan.moe/v4",
            httpClient = httpClient
        )
    }
    
    /**
     * Jikan API 客户端
     */
    private val jikanApi: JikanApi by lazy {
        createJikanApi(jikanClient)
    }
    
    /**
     * Jikan 到 Discover 的数据转换器
     */
    private val jikanToDiscoverMapper: JikanToDiscoverMapper by lazy {
        JikanToDiscoverMapper()
    }
    
    /**
     * Discover Repository 实现
     * 
     * Android 使用 Jikan API 作为数据源
     * 可以根据需要切换为缓存优先或离线模式
     */
    private val discoverRepository: DiscoverRepository by lazy {
        JikanDiscoverRepository(
            jikanApi = jikanApi,
            mapper = jikanToDiscoverMapper
        )
    }
    
    // ========================================
    // Domain 层：UseCase
    // ========================================
    
    /**
     * 获取热门动漫的 UseCase
     */
    private val getTrendingAnimeUseCase: GetTrendingAnimeUseCase by lazy {
        GetTrendingAnimeUseCase(
            repository = discoverRepository
        )
    }
    
    /**
     * 获取本季新番的 UseCase
     */
    private val getCurrentSeasonAnimeUseCase: com.pusu.indexed.domain.discover.usecase.GetCurrentSeasonAnimeUseCase by lazy {
        com.pusu.indexed.domain.discover.usecase.GetCurrentSeasonAnimeUseCase(
            repository = discoverRepository
        )
    }
    
    /**
     * 获取随机动漫的 UseCase
     */
    private val getRandomAnimeUseCase: com.pusu.indexed.domain.discover.usecase.GetRandomAnimeUseCase by lazy {
        com.pusu.indexed.domain.discover.usecase.GetRandomAnimeUseCase(
            repository = discoverRepository
        )
    }
    
    // ========================================
    // Feature 层：ViewModel
    // ========================================
    
    /**
     * 创建 DiscoverViewModel
     * 
     * @param coroutineScope 协程作用域（由调用方提供，用于管理生命周期）
     */
    fun createDiscoverViewModel(coroutineScope: CoroutineScope): DiscoverViewModel {
        return DiscoverViewModel(
            getTrendingAnimeUseCase = getTrendingAnimeUseCase,
            getCurrentSeasonAnimeUseCase = getCurrentSeasonAnimeUseCase,
            getRandomAnimeUseCase = getRandomAnimeUseCase,
            coroutineScope = coroutineScope
        )
    }
    
    // ========================================
    // 未来可以添加更多功能
    // ========================================
    
    // fun createSearchViewModel(...)
    // fun createDetailViewModel(...)
    // fun createFavoriteViewModel(...)
}

