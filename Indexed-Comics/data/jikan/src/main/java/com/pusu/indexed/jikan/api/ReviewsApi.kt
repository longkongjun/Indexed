package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.AnimeReview
import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.manga.MangaReview
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 评论相关 API 接口
 * 提供最新评论查询功能
 */
interface ReviewsApi {
    
    /**
     * 获取最新动漫评论
     * 
     * @param page 页码（可选）
     * @param type 类型筛选（可选）
     * @param preliminary 是否包含初步评论（可选）
     * @param spoiler 是否包含剧透评论（可选）
     * @return 评论列表分页响应
     */
    @GET("reviews/anime")
    suspend fun getRecentAnimeReviews(
        @Query("page") page: Int? = null,
        @Query("type") type: String? = null,
        @Query("preliminary") preliminary: Boolean? = null,
        @Query("spoiler") spoiler: Boolean? = null
    ): Result<JikanPageResponse<AnimeReview>>
    
    /**
     * 获取最新漫画评论
     * 
     * @param page 页码（可选）
     * @param type 类型筛选（可选）
     * @param preliminary 是否包含初步评论（可选）
     * @param spoiler 是否包含剧透评论（可选）
     * @return 评论列表分页响应
     */
    @GET("reviews/manga")
    suspend fun getRecentMangaReviews(
        @Query("page") page: Int? = null,
        @Query("type") type: String? = null,
        @Query("preliminary") preliminary: Boolean? = null,
        @Query("spoiler") spoiler: Boolean? = null
    ): Result<JikanPageResponse<MangaReview>>
}

