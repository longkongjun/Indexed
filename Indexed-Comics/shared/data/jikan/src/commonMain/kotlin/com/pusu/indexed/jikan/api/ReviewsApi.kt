package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.review.RecentReview
import com.pusu.indexed.jikan.models.common.*

/**
 * Reviews 评论 API。
 * 
 * 提供全局评论相关的接口，包括：
 * - 获取最新动漫评论
 * - 获取最新漫画评论
 * 
 * 注意：这是全局评论列表，不是单个作品的评论。
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/reviews
 */
interface ReviewsApi {
    /**
     * 获取最新动漫评论列表。
     * 
     * 路径：`GET /reviews/anime`
     * 
     * @param page 页码，从 1 开始
     * @param preliminary 是否包含初步评论
     * @param spoiler 是否包含剧透评论
     * @return 最新动漫评论列表的响应结果
     */
    suspend fun getRecentAnimeReviews(
        page: Int? = null,
        preliminary: Boolean? = null,
        spoiler: Boolean? = null
    ): Result<JikanPageResponse<RecentReview>>
    
    /**
     * 获取最新漫画评论列表。
     * 
     * 路径：`GET /reviews/manga`
     * 
     * @param page 页码，从 1 开始
     * @param preliminary 是否包含初步评论
     * @param spoiler 是否包含剧透评论
     * @return 最新漫画评论列表的响应结果
     */
    suspend fun getRecentMangaReviews(
        page: Int? = null,
        preliminary: Boolean? = null,
        spoiler: Boolean? = null
    ): Result<JikanPageResponse<RecentReview>>
}

