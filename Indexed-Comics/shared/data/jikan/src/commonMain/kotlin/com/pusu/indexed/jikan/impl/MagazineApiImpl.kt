package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.MagazineApi
import com.pusu.indexed.jikan.models.magazine.Magazine
import com.pusu.indexed.jikan.models.common.*

/**
 * MagazineApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class MagazineApiImpl(
    private val client: JikanClient
) : MagazineApi {
    
    override suspend fun searchMagazines(
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sort: String?
    ): Result<JikanPageResponse<Magazine>> =
        client.get(
            path = "magazines",
            query = buildMap {
                query?.let { put("q", it) }
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                orderBy?.let { put("order_by", it) }
                sort?.let { put("sort", it) }
            }
        )
}
