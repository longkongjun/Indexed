package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.RecommendationsApi
import com.pusu.indexed.jikan.models.recommendation.RecentRecommendation
import com.pusu.indexed.jikan.models.common.*

/**
 * RecommendationsApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class RecommendationsApiImpl(
    private val client: JikanClient
) : RecommendationsApi {
    
    override suspend fun getRecentAnimeRecommendations(
        page: Int?
    ): Result<JikanPageResponse<RecentRecommendation>> =
        client.get(
            path = "recommendations/anime",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
    
    override suspend fun getRecentMangaRecommendations(
        page: Int?
    ): Result<JikanPageResponse<RecentRecommendation>> =
        client.get(
            path = "recommendations/manga",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
}
