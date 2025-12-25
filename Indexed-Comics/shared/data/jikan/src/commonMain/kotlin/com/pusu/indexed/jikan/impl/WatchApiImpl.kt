package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.WatchApi
import com.pusu.indexed.jikan.models.watch.*
import com.pusu.indexed.jikan.models.common.*

/**
 * WatchApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class WatchApiImpl(
    private val client: JikanClient
) : WatchApi {
    
    override suspend fun getRecentPromoVideos(
        page: Int?
    ): Result<JikanPageResponse<RecentPromoVideo>> =
        client.get(
            path = "watch/promos",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
    
    override suspend fun getRecentEpisodeVideos(
        page: Int?
    ): Result<JikanPageResponse<RecentEpisodeVideo>> =
        client.get(
            path = "watch/episodes",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
    
    override suspend fun getPopularPromoVideos(
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<RecentPromoVideo>> =
        client.get(
            path = "watch/promos/popular",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
    
    override suspend fun getPopularEpisodeVideos(
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<RecentEpisodeVideo>> =
        client.get(
            path = "watch/episodes/popular",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
}
