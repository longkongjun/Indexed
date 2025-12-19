package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.magazine.Magazine
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 杂志/出版社相关 API 接口
 * 提供杂志出版社信息查询功能
 */
interface MagazinesApi {
    
    /**
     * 获取杂志列表
     * 
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param query 搜索关键词（可选）
     * @param orderBy 排序字段（mal_id, name, count）
     * @param sort 排序方向（asc, desc）
     * @param letter 首字母筛选（可选）
     * @return 杂志列表分页响应
     */
    @GET("magazines")
    suspend fun getMagazines(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("q") query: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("letter") letter: String? = null
    ): Result<JikanPageResponse<Magazine>>
    
    /**
     * 获取杂志详情
     * 
     * @param id 杂志的 MAL ID
     * @return 杂志信息响应
     */
    @GET("magazines/{id}")
    suspend fun getMagazineById(
        @Path("id") id: Int
    ): Result<JikanResponse<Magazine>>
}

