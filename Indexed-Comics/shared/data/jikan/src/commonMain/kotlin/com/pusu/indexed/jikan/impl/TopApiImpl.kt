package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.TopApi
import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.manga.Manga
import com.pusu.indexed.jikan.models.character.Character
import com.pusu.indexed.jikan.models.people.Person
import com.pusu.indexed.jikan.models.common.*

/**
 * TopApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class TopApiImpl(
    private val client: JikanClient
) : TopApi {
    
    override suspend fun getTopAnime(
        page: Int?,
        limit: Int?,
        filter: String?,
        type: String?
    ): Result<JikanPageResponse<Anime>> =
        client.get(
            path = "top/anime",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                filter?.let { put("filter", it) }
                type?.let { put("type", it) }
            }
        )
    
    override suspend fun getTopManga(
        page: Int?,
        limit: Int?,
        filter: String?,
        type: String?
    ): Result<JikanPageResponse<Manga>> =
        client.get(
            path = "top/manga",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                filter?.let { put("filter", it) }
                type?.let { put("type", it) }
            }
        )
    
    override suspend fun getTopCharacters(
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<Character>> =
        client.get(
            path = "top/characters",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
    
    override suspend fun getTopPeople(
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<Person>> =
        client.get(
            path = "top/people",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
    
    override suspend fun getTopReviews(
        page: Int?
    ): Result<JikanPageResponse<com.pusu.indexed.jikan.models.review.RecentReview>> =
        client.get(
            path = "top/reviews",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
}
