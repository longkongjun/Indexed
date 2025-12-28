package com.pusu.indexed.data.jikan.repository

import com.pusu.indexed.domain.discover.model.AnimeItem
import com.pusu.indexed.domain.discover.repository.DiscoverRepository
import com.pusu.indexed.data.jikan.mapper.JikanToDiscoverMapper
import com.pusu.indexed.jikan.JikanApi

/**
 * Jikan API 实现的 Discover 仓库
 * 
 * 这是 Data 层的实现，实现了 Domain 层定义的 Repository 接口。
 * 
 * 职责：
 * - 实现 DiscoverRepository 接口
 * - 调用 Jikan API 获取数据
 * - 使用 Mapper 将 API 模型转换为 Domain 模型
 * - 处理网络错误和异常
 * 
 * 优势：
 * - ✅ 实现了接口：遵循依赖倒置原则
 * - ✅ 可替换：可以轻松替换为其他数据源（本地数据库、其他API）
 * - ✅ 职责单一：只负责数据获取，不包含业务逻辑
 * 
 * @property jikanApi Jikan API 客户端
 * @property mapper 数据映射器
 */
class JikanDiscoverRepository(
    private val jikanApi: JikanApi,
    private val mapper: JikanToDiscoverMapper
) : DiscoverRepository {
    
    override suspend fun getTrendingAnime(
        page: Int,
        limit: Int
    ): Result<List<AnimeItem>> = runCatching {
        // 1. 调用 Jikan API
        val response = jikanApi.top.getTopAnime(
            page = page,
            limit = limit,
            filter = "bypopularity"  // 按人气排序
        ).getOrThrow()
        
        // 2. 映射为 Domain 模型
        mapper.mapToAnimeItemList(response.data ?: emptyList())
    }
    
    override suspend fun getCurrentSeasonAnime(
        page: Int,
        limit: Int
    ): Result<List<AnimeItem>> = runCatching {
        val response = jikanApi.seasons.getCurrentSeasonAnime(
            page = page,
            limit = limit
        ).getOrThrow()
        
        mapper.mapToAnimeItemList(response.data ?: emptyList())
    }
    
    override suspend fun getRandomAnime(): Result<AnimeItem> = runCatching {
        val response = jikanApi.random.getRandomAnime().getOrThrow()
        val anime = response.data ?: throw IllegalStateException("Random anime data is null")
        mapper.mapToAnimeItem(anime)
    }
    
    override suspend fun getTopRankedAnime(
        rankingType: String,
        page: Int,
        limit: Int
    ): Result<List<AnimeItem>> = runCatching {
        val response = jikanApi.top.getTopAnime(
            page = page,
            limit = limit,
            filter = rankingType
        ).getOrThrow()
        
        mapper.mapToAnimeItemList(response.data ?: emptyList())
    }
    
    override suspend fun searchAnime(
        query: String,
        page: Int,
        limit: Int
    ): Result<List<AnimeItem>> = runCatching {
        val response = jikanApi.anime.searchAnime(
            query = query,
            page = page,
            limit = limit
        ).getOrThrow()
        
        mapper.mapToAnimeItemList(response.data ?: emptyList())
    }
}

