package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.recommendation.RecentRecommendation
import com.pusu.indexed.jikan.models.common.*

/**
 * Recommendations 推荐 API。
 * 
 * 提供全局推荐相关的接口，包括：
 * - 获取最新动漫推荐
 * - 获取最新漫画推荐
 * 
 * 注意：这是全局推荐列表，不是单个作品的推荐。
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/recommendations
 */
interface RecommendationsApi {
    /**
     * 获取最新动漫推荐列表。
     * 
     * 路径：`GET /recommendations/anime`
     * 
     * @param page 页码，从 1 开始
     * @return 最新动漫推荐列表的响应结果
     */
    suspend fun getRecentAnimeRecommendations(
        page: Int? = null
    ): Result<JikanPageResponse<RecentRecommendation>>
    
    /**
     * 获取最新漫画推荐列表。
     * 
     * 路径：`GET /recommendations/manga`
     * 
     * @param page 页码，从 1 开始
     * @return 最新漫画推荐列表的响应结果
     */
    suspend fun getRecentMangaRecommendations(
        page: Int? = null
    ): Result<JikanPageResponse<RecentRecommendation>>
}

