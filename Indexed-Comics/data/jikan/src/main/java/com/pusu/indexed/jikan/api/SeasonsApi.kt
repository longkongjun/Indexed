package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.season.Season
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 季度动漫相关 API 接口
 * 提供季度动漫列表查询功能
 */
interface SeasonsApi {
    
    /**
     * 获取指定季度动漫列表
     * 
     * @param year 年份
     * @param season 季度（winter, spring, summer, fall）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param filter 类型筛选（tv, movie, ova, special, ona, music）
     * @param sfw 是否仅显示安全内容（可选）
     * @return 动漫列表分页响应
     */
    @GET("seasons/{year}/{season}")
    suspend fun getSeasonAnime(
        @Path("year") year: Int,
        @Path("season") season: String,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("filter") filter: String? = null,
        @Query("sfw") sfw: Boolean? = null
    ): Result<JikanPageResponse<Anime>>
    
    /**
     * 获取当前季度动漫列表
     * 
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param filter 类型筛选（tv, movie, ova, special, ona, music）
     * @param sfw 是否仅显示安全内容（可选）
     * @return 动漫列表分页响应
     */
    @GET("seasons/now")
    suspend fun getCurrentSeasonAnime(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("filter") filter: String? = null,
        @Query("sfw") sfw: Boolean? = null
    ): Result<JikanPageResponse<Anime>>
    
    /**
     * 获取即将播出动漫列表
     * 
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param filter 类型筛选（tv, movie, ova, special, ona, music）
     * @param sfw 是否仅显示安全内容（可选）
     * @return 动漫列表分页响应
     */
    @GET("seasons/upcoming")
    suspend fun getUpcomingSeasonAnime(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("filter") filter: String? = null,
        @Query("sfw") sfw: Boolean? = null
    ): Result<JikanPageResponse<Anime>>
    
    /**
     * 获取所有可用季度列表
     * 
     * @return 季度列表响应
     */
    @GET("seasons")
    suspend fun getSeasonsList(): Result<JikanPageResponse<Season>>
}

