package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.common.*

/**
 * Schedules 播放时间表 API。
 * 
 * 提供当前季度动漫播放时间表的接口。
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/schedules
 */
interface SchedulesApi {
    /**
     * 获取当前季度动漫播放时间表。
     * 
     * 路径：`GET /schedules`
     * 
     * @param filter 过滤条件：monday, tuesday, wednesday, thursday, friday, saturday, sunday, other, unknown
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @return 播放时间表的响应结果
     */
    suspend fun getSchedules(
        filter: String? = null,
        page: Int? = null,
        limit: Int? = null
    ): Result<JikanPageResponse<Anime>>
}

