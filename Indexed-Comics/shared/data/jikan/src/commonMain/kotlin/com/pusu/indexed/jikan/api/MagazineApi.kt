package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.magazine.Magazine
import com.pusu.indexed.jikan.models.common.*

/**
 * Magazine 杂志/出版社 API。
 * 
 * 提供杂志/出版社相关的所有接口，包括：
 * - 搜索杂志
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/magazines
 */
interface MagazineApi {
    /**
     * 搜索杂志/出版社。
     * 
     * 路径：`GET /magazines`
     * 
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param orderBy 排序字段：mal_id, name, count
     * @param sort 排序方向：asc, desc
     * @return 搜索结果的响应
     */
    suspend fun searchMagazines(
        query: String? = null,
        page: Int? = null,
        limit: Int? = null,
        orderBy: String? = null,
        sort: String? = null
    ): Result<JikanPageResponse<Magazine>>
}

