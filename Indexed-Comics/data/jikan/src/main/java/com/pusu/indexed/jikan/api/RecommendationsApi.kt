package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.common.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 推荐相关 API 接口
 * 提供最新推荐查询功能
 */
interface RecommendationsApi {
    
    /**
     * 获取最新动漫推荐
     * 
     * @param page 页码（可选）
     * @return 推荐列表分页响应
     */
    @GET("recommendations/anime")
    suspend fun getRecentAnimeRecommendations(
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<Recommendation>>
    
    /**
     * 获取最新漫画推荐
     * 
     * @param page 页码（可选）
     * @return 推荐列表分页响应
     */
    @GET("recommendations/manga")
    suspend fun getRecentMangaRecommendations(
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<Recommendation>>
}

