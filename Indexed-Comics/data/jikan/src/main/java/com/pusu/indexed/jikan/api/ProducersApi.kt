package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.producer.Producer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 制作公司相关 API 接口
 * 提供制作公司信息查询功能
 */
interface ProducersApi {
    
    /**
     * 获取制作公司列表
     * 
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param query 搜索关键词（可选）
     * @param orderBy 排序字段（mal_id, name, count, favorites, established）
     * @param sort 排序方向（asc, desc）
     * @param letter 首字母筛选（可选）
     * @return 制作公司列表分页响应
     */
    @GET("producers")
    suspend fun getProducers(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("q") query: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("letter") letter: String? = null
    ): Result<JikanPageResponse<Producer>>
    
    /**
     * 获取制作公司基本信息
     * 
     * @param id 制作公司的 MAL ID
     * @return 制作公司信息响应
     */
    @GET("producers/{id}")
    suspend fun getProducerById(
        @Path("id") id: Int
    ): Result<JikanResponse<Producer>>
    
    /**
     * 获取制作公司完整信息
     * 
     * @param id 制作公司的 MAL ID
     * @return 制作公司完整信息响应
     */
    @GET("producers/{id}/full")
    suspend fun getProducerFullById(
        @Path("id") id: Int
    ): Result<JikanResponse<Producer>>
    
    /**
     * 获取制作公司外部链接
     * 
     * @param id 制作公司的 MAL ID
     * @return 外部链接列表响应
     */
    @GET("producers/{id}/external")
    suspend fun getProducerExternal(
        @Path("id") id: Int
    ): Result<JikanResponse<List<ExternalLink>>>
}

