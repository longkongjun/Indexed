package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.watch.*
import retrofit2.http.GET

/**
 * 观看相关 API 接口
 * 提供最近添加的视频查询功能
 */
interface WatchApi {
    
    /**
     * 获取最近添加的宣传视频
     * 
     * @return 宣传视频列表响应
     */
    @GET("watch/promos")
    suspend fun getRecentPromos(): Result<JikanResponse<List<RecentPromoVideo>>>
    
    /**
     * 获取最近添加的剧集视频
     * 
     * @return 剧集视频列表响应
     */
    @GET("watch/episodes")
    suspend fun getRecentEpisodes(): Result<JikanResponse<List<RecentEpisodeVideo>>>
    
    /**
     * 获取热门宣传视频
     * 
     * @return 宣传视频列表响应
     */
    @GET("watch/promos/popular")
    suspend fun getPopularPromos(): Result<JikanResponse<List<RecentPromoVideo>>>
}

