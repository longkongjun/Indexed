package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.ProducerApi
import com.pusu.indexed.jikan.models.producer.Producer
import com.pusu.indexed.jikan.models.common.*

/**
 * ProducerApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class ProducerApiImpl(
    private val client: JikanClient
) : ProducerApi {
    
    override suspend fun getProducerById(id: Int): Result<JikanResponse<Producer>> =
        client.get(path = "producers/$id")
    
    override suspend fun getProducerFullById(id: Int): Result<JikanResponse<Producer>> =
        client.get(path = "producers/$id/full")
    
    override suspend fun searchProducers(
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sort: String?
    ): Result<JikanPageResponse<Producer>> =
        client.get(
            path = "producers",
            query = buildMap {
                query?.let { put("q", it) }
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                orderBy?.let { put("order_by", it) }
                sort?.let { put("sort", it) }
            }
        )
}
