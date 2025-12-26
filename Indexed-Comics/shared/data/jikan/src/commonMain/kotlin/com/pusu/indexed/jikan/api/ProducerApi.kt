package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.producer.Producer
import com.pusu.indexed.jikan.models.common.*

/**
 * Producer 制作公司 API。
 * 
 * 提供制作公司相关的所有接口，包括：
 * - 获取制作公司信息
 * - 获取制作公司的完整信息
 * - 搜索制作公司
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/producers
 */
interface ProducerApi {
    /**
     * 获取制作公司的基础信息。
     * 
     * 路径：`GET /producers/{id}`
     * 
     * @param id MAL 制作公司 ID（必需）
     * @return 制作公司信息的响应结果
     */
    suspend fun getProducerById(id: Int): Result<JikanResponse<Producer>>
    
    /**
     * 获取制作公司的完整信息。
     * 
     * 路径：`GET /producers/{id}/full`
     * 
     * @param id MAL 制作公司 ID（必需）
     * @return 完整制作公司信息的响应结果
     */
    suspend fun getProducerFullById(id: Int): Result<JikanResponse<Producer>>
    
    /**
     * 搜索制作公司。
     * 
     * 路径：`GET /producers`
     * 
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param orderBy 排序字段：mal_id, name, count, favorites, established
     * @param sort 排序方向：asc, desc
     * @return 搜索结果的响应
     */
    suspend fun searchProducers(
        query: String? = null,
        page: Int? = null,
        limit: Int? = null,
        orderBy: String? = null,
        sort: String? = null
    ): Result<JikanPageResponse<Producer>>
}

