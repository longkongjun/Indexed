package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.MangaApi
import com.pusu.indexed.jikan.api.MangaFilter
import com.pusu.indexed.jikan.models.manga.*
import com.pusu.indexed.jikan.models.common.*

/**
 * MangaApi 的默认实现。
 * 
 * 职责：
 * - 将接口方法调用转换为对应的 HTTP 请求路径和参数
 * - 委托 JikanClient 执行实际的网络请求
 * - 不包含业务逻辑，仅负责参数组装和过滤
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class MangaApiImpl(
    private val client: JikanClient
) : MangaApi {
    
    override suspend fun getMangaById(id: Int): Result<JikanResponse<Manga>> =
        client.get(path = "manga/$id")
    
    override suspend fun getMangaFullById(id: Int): Result<JikanResponse<Manga>> =
        client.get(path = "manga/$id/full")
    
    override suspend fun getMangaCharacters(id: Int): Result<JikanResponse<List<MangaCharacter>>> =
        client.get(path = "manga/$id/characters")
    
    override suspend fun getMangaNews(
        id: Int,
        page: Int?
    ): Result<JikanPageResponse<MangaNews>> =
        client.get(
            path = "manga/$id/news",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
    
    override suspend fun getMangaForum(
        id: Int,
        filter: String?
    ): Result<JikanResponse<List<com.pusu.indexed.jikan.models.anime.ForumTopic>>> =
        client.get(
            path = "manga/$id/forum",
            query = buildMap {
                filter?.let { put("filter", it) }
            }
        )
    
    override suspend fun getMangaPictures(id: Int): Result<JikanResponse<List<MangaPicture>>> =
        client.get(path = "manga/$id/pictures")
    
    override suspend fun getMangaStatistics(id: Int): Result<JikanResponse<MangaStatistics>> =
        client.get(path = "manga/$id/statistics")
    
    override suspend fun getMangaMoreInfo(id: Int): Result<JikanResponse<String>> =
        client.get(path = "manga/$id/moreinfo")
    
    override suspend fun getMangaRecommendations(id: Int): Result<JikanResponse<List<Recommendation>>> =
        client.get(path = "manga/$id/recommendations")
    
    override suspend fun getMangaUserUpdates(
        id: Int,
        page: Int?
    ): Result<JikanPageResponse<UserUpdate>> =
        client.get(
            path = "manga/$id/userupdates",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
    
    override suspend fun getMangaReviews(
        id: Int,
        page: Int?
    ): Result<JikanPageResponse<MangaReview>> =
        client.get(
            path = "manga/$id/reviews",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
    
    override suspend fun searchManga(
        query: String?,
        page: Int?,
        limit: Int?,
        type: String?,
        score: Double?,
        status: String?,
        sfw: Boolean?,
        genres: String?,
        orderBy: String?,
        sort: String?
    ): Result<JikanPageResponse<Manga>> =
        client.get(
            path = "manga",
            query = buildMap {
                query?.let { put("q", it) }
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                type?.let { put("type", it) }
                score?.let { put("score", it) }
                status?.let { put("status", it) }
                sfw?.let { put("sfw", it) }
                genres?.let { put("genres", it) }
                orderBy?.let { put("order_by", it) }
                sort?.let { put("sort", it) }
            }
        )
    
    override suspend fun getTopManga(
        page: Int?,
        limit: Int?,
        filter: MangaFilter?
    ): Result<JikanPageResponse<Manga>> =
        client.get(
            path = "top/manga",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                filter?.let { put("filter", it.value) }
            }
        )
}
