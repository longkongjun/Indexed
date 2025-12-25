package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.SeasonApi
import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.season.Season
import com.pusu.indexed.jikan.models.common.*

/**
 * SeasonApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class SeasonApiImpl(
    private val client: JikanClient
) : SeasonApi {
    
    override suspend fun getSeasonAnime(
        year: Int,
        season: String,
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<Anime>> =
        client.get(
            path = "seasons/$year/$season",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
    
    override suspend fun getCurrentSeasonAnime(
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<Anime>> =
        client.get(
            path = "seasons/now",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
    
    override suspend fun getUpcomingSeasonAnime(
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<Anime>> =
        client.get(
            path = "seasons/upcoming",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
    
    override suspend fun getAllSeasons(): Result<JikanResponse<List<Season>>> =
        client.get(path = "seasons")
}
