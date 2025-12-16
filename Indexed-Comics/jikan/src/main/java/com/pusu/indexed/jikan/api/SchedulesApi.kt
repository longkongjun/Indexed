package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.common.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 播放时间表相关 API 接口
 * 提供动漫播放时间表查询功能
 */
interface SchedulesApi {
    
    /**
     * 获取动漫播放时间表
     * 
     * @param filter 星期筛选（monday, tuesday, wednesday, thursday, friday, saturday, sunday, unknown, other）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param kids 是否仅显示儿童向内容（可选）
     * @param sfw 是否仅显示安全内容（可选）
     * @return 动漫列表分页响应
     */
    @GET("schedules")
    suspend fun getSchedules(
        @Query("filter") filter: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("kids") kids: Boolean? = null,
        @Query("sfw") sfw: Boolean? = null
    ): Result<JikanPageResponse<Anime>>
}

