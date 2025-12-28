package com.pusu.indexed.domain.discover.repository

import com.pusu.indexed.domain.discover.model.AnimeItem

/**
 * Discover 仓库接口
 * 
 * 这是 Domain 层定义的抽象接口，Data 层需要实现它。
 * 
 * 职责：
 * - 定义获取数据的抽象方法
 * - 不关心数据来自哪里（API、数据库、缓存等）
 * - 返回 Domain 层的模型（AnimeItem），而不是 API 模型
 * 
 * 优势：
 * - ✅ 完全解耦：Domain 层不依赖 Data 层的具体实现
 * - ✅ 易于替换：可以随时切换数据源（API → 数据库 → Mock）
 * - ✅ 易于测试：可以轻松创建 Mock 实现进行测试
 * - ✅ 符合 Clean Architecture 原则
 */
interface DiscoverRepository {
    
    /**
     * 获取热门动漫
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量
     * @return Result<List<AnimeItem>> 成功返回动漫列表，失败返回异常
     */
    suspend fun getTrendingAnime(
        page: Int = 1,
        limit: Int = 10
    ): Result<List<AnimeItem>>
    
    /**
     * 获取本季新番
     * 
     * @param page 页码
     * @param limit 每页数量
     * @return Result<List<AnimeItem>>
     */
    suspend fun getCurrentSeasonAnime(
        page: Int = 1,
        limit: Int = 25
    ): Result<List<AnimeItem>>
    
    /**
     * 获取随机动漫
     * 
     * @return Result<AnimeItem>
     */
    suspend fun getRandomAnime(): Result<AnimeItem>
    
    /**
     * 按排名类型获取动漫排行榜
     * 
     * @param rankingType 排名类型（人气、评分、收藏等）
     * @param page 页码
     * @param limit 每页数量
     * @return Result<List<AnimeItem>>
     */
    suspend fun getTopRankedAnime(
        rankingType: String = "",  // "" = 按评分, "bypopularity" = 按人气
        page: Int = 1,
        limit: Int = 10
    ): Result<List<AnimeItem>>
    
    /**
     * 搜索动漫
     * 
     * @param query 搜索关键词
     * @param page 页码
     * @param limit 每页数量
     * @return Result<List<AnimeItem>>
     */
    suspend fun searchAnime(
        query: String,
        page: Int = 1,
        limit: Int = 20
    ): Result<List<AnimeItem>>
}

