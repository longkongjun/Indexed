package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.SchedulesApi
import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.common.*

/**
 * SchedulesApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class SchedulesApiImpl(
    private val client: JikanClient
) : SchedulesApi {
    
    override suspend fun getSchedules(
        filter: String?,
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<Anime>> =
        client.get(
            path = "schedules",
            query = buildMap {
                filter?.let { put("filter", it) }
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
}
