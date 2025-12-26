package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.watch.*
import com.pusu.indexed.jikan.models.common.*

/**
 * Watch 观看推荐 API。
 * 
 * 提供最新宣传视频和剧集视频的接口。
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/watch
 */
interface WatchApi {
    /**
     * 获取最新的宣传视频列表。
     * 
     * 路径：`GET /watch/promos`
     * 
     * @param page 页码，从 1 开始
     * @return 宣传视频列表的响应结果
     */
    suspend fun getRecentPromoVideos(
        page: Int? = null
    ): Result<JikanPageResponse<RecentPromoVideo>>
    
    /**
     * 获取最新的剧集视频列表。
     * 
     * 路径：`GET /watch/episodes`
     * 
     * @param page 页码，从 1 开始
     * @return 剧集视频列表的响应结果
     */
    suspend fun getRecentEpisodeVideos(
        page: Int? = null
    ): Result<JikanPageResponse<RecentEpisodeVideo>>
    
    /**
     * 获取热门宣传视频列表。
     * 
     * 路径：`GET /watch/promos/popular`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量
     * @return 热门宣传视频列表的响应结果
     */
    suspend fun getPopularPromoVideos(
        page: Int? = null,
        limit: Int? = null
    ): Result<JikanPageResponse<RecentPromoVideo>>
    
    /**
     * 获取热门剧集视频列表。
     * 
     * 路径：`GET /watch/episodes/popular`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量
     * @return 热门剧集视频列表的响应结果
     */
    suspend fun getPopularEpisodeVideos(
        page: Int? = null,
        limit: Int? = null
    ): Result<JikanPageResponse<RecentEpisodeVideo>>
}

