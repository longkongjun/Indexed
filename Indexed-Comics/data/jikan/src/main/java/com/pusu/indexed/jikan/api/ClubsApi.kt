package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.club.*
import com.pusu.indexed.jikan.models.common.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 俱乐部相关 API 接口
 * 提供俱乐部信息查询功能
 */
interface ClubsApi {
    
    /**
     * 获取俱乐部列表
     * 
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param query 搜索关键词（可选）
     * @param type 俱乐部类型（可选）
     * @param category 俱乐部分类（anime, manga, actors_and_artists, characters, cities_and_neighborhoods, companies, conventions, games, japan, music, other, schools）
     * @param orderBy 排序字段（mal_id, name, members_count, pictures_count, created）
     * @param sort 排序方向（asc, desc）
     * @param letter 首字母筛选（可选）
     * @return 俱乐部列表分页响应
     */
    @GET("clubs")
    suspend fun getClubs(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("q") query: String? = null,
        @Query("type") type: String? = null,
        @Query("category") category: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("letter") letter: String? = null
    ): Result<JikanPageResponse<Club>>
    
    /**
     * 获取俱乐部详情
     * 
     * @param id 俱乐部的 MAL ID
     * @return 俱乐部信息响应
     */
    @GET("clubs/{id}")
    suspend fun getClubById(
        @Path("id") id: Int
    ): Result<JikanResponse<Club>>
    
    /**
     * 获取俱乐部成员列表
     * 
     * @param id 俱乐部的 MAL ID
     * @param page 页码（可选）
     * @return 成员列表分页响应
     */
    @GET("clubs/{id}/members")
    suspend fun getClubMembers(
        @Path("id") id: Int,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<Club>>
    
    /**
     * 获取俱乐部工作人员列表
     * 
     * @param id 俱乐部的 MAL ID
     * @return 工作人员列表响应
     */
    @GET("clubs/{id}/staff")
    suspend fun getClubStaff(
        @Path("id") id: Int
    ): Result<JikanResponse<Club>>
    
    /**
     * 获取俱乐部关系
     * 
     * @param id 俱乐部的 MAL ID
     * @return 俱乐部关系信息响应
     */
    @GET("clubs/{id}/relations")
    suspend fun getClubRelations(
        @Path("id") id: Int
    ): Result<JikanResponse<ClubMember>>
}

