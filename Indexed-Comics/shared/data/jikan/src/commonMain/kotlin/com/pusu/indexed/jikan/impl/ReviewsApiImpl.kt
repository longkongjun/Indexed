package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.ReviewsApi
import com.pusu.indexed.jikan.models.review.RecentReview
import com.pusu.indexed.jikan.models.common.*

/**
 * ReviewsApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class ReviewsApiImpl(
    private val client: JikanClient
) : ReviewsApi {
    
    override suspend fun getRecentAnimeReviews(
        page: Int?,
        preliminary: Boolean?,
        spoiler: Boolean?
    ): Result<JikanPageResponse<RecentReview>> =
        client.get(
            path = "reviews/anime",
            query = buildMap {
                page?.let { put("page", it) }
                preliminary?.let { put("preliminary", it) }
                spoiler?.let { put("spoiler", it) }
            }
        )
    
    override suspend fun getRecentMangaReviews(
        page: Int?,
        preliminary: Boolean?,
        spoiler: Boolean?
    ): Result<JikanPageResponse<RecentReview>> =
        client.get(
            path = "reviews/manga",
            query = buildMap {
                page?.let { put("page", it) }
                preliminary?.let { put("preliminary", it) }
                spoiler?.let { put("spoiler", it) }
            }
        )
}
