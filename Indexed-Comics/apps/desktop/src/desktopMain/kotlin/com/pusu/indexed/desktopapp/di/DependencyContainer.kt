package com.pusu.indexed.desktopapp.di

import com.pusu.indexed.data.jikan.mapper.JikanToDiscoverMapper
import com.pusu.indexed.data.jikan.mapper.JikanToAnimeDetailMapper
import com.pusu.indexed.data.jikan.repository.JikanDiscoverRepository
import com.pusu.indexed.data.jikan.repository.JikanAnimeDetailRepository
import com.pusu.indexed.data.jikan.repository.JikanRelatedAnimeRepository
import com.pusu.indexed.data.jikan.repository.JikanAnimeRecommendationsRepository
import com.pusu.indexed.domain.discover.repository.DiscoverRepository
import com.pusu.indexed.domain.discover.usecase.GetTrendingAnimeUseCase
import com.pusu.indexed.domain.discover.usecase.GetTopAnimeUseCase
import com.pusu.indexed.domain.feed.usecase.GetAnimeDetailUseCase
import com.pusu.indexed.domain.feed.usecase.GetRelatedAnimeUseCase
import com.pusu.indexed.domain.feed.usecase.GetAnimeRecommendationsUseCase
import com.pusu.indexed.jikan.JikanApi
import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.createJikanApi
import com.pusu.indexed.shared.feature.discover.presentation.DiscoverViewModel
import com.pusu.indexed.shared.feature.animedetail.presentation.AnimeDetailViewModel
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope

/**
 * Desktop 应用的依赖注入容器
 * 
 * 负责创建和管理所有依赖关系。
 * Desktop 可能有不同的配置需求（如本地数据库优先）
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
     * Jikan 到 AnimeDetail 的数据转换器
     */
    private val jikanToAnimeDetailMapper: JikanToAnimeDetailMapper by lazy {
        JikanToAnimeDetailMapper()
    }
    
    /**
     * Discover Repository 实现
     * 
     * Desktop 使用 Jikan API
     * 未来可以考虑添加本地缓存
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
     * 获取排行榜动漫的 UseCase
     */
    private val getTopAnimeUseCase: GetTopAnimeUseCase by lazy {
        GetTopAnimeUseCase(
            repository = discoverRepository
        )
    }
    
    /**
     * 获取动漫详情的 UseCase
     */
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase by lazy {
        JikanAnimeDetailRepository(
            jikanApi = jikanApi,
            mapper = jikanToAnimeDetailMapper
        )
    }
    
    /**
     * 获取相关动漫的 UseCase
     */
    private val getRelatedAnimeUseCase: GetRelatedAnimeUseCase by lazy {
        JikanRelatedAnimeRepository(
            jikanApi = jikanApi,
            mapper = jikanToAnimeDetailMapper
        )
    }
    
    /**
     * 获取推荐动漫的 UseCase
     */
    private val getAnimeRecommendationsUseCase: GetAnimeRecommendationsUseCase by lazy {
        JikanAnimeRecommendationsRepository(
            jikanApi = jikanApi,
            mapper = jikanToAnimeDetailMapper
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
            getTopAnimeUseCase = getTopAnimeUseCase,
            coroutineScope = coroutineScope
        )
    }
    
    /**
     * 创建 AnimeDetailViewModel
     * 
     * @param coroutineScope 协程作用域（由调用方提供，用于管理生命周期）
     */
    fun createAnimeDetailViewModel(coroutineScope: CoroutineScope): AnimeDetailViewModel {
        return AnimeDetailViewModel(
            getAnimeDetailUseCase = getAnimeDetailUseCase,
            getRelatedAnimeUseCase = getRelatedAnimeUseCase,
            getAnimeRecommendationsUseCase = getAnimeRecommendationsUseCase,
            coroutineScope = coroutineScope
        )
    }
    
    // ========================================
    // 未来可以添加更多功能
    // ========================================
    
    // fun createSearchViewModel(...)
}

