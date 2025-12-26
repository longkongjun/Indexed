package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.club.*
import com.pusu.indexed.jikan.models.common.*

/**
 * Club 俱乐部 API。
 * 
 * 提供俱乐部相关的所有接口，包括：
 * - 获取俱乐部信息
 * - 获取俱乐部成员
 * - 获取俱乐部工作人员
 * - 获取俱乐部关系
 * - 搜索俱乐部
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/clubs
 */
interface ClubApi {
    /**
     * 获取俱乐部的基础信息。
     * 
     * 路径：`GET /clubs/{id}`
     * 
     * @param id MAL 俱乐部 ID（必需）
     * @return 俱乐部信息的响应结果
     */
    suspend fun getClubById(id: Int): Result<JikanResponse<Club>>
    
    /**
     * 获取俱乐部的成员列表。
     * 
     * 路径：`GET /clubs/{id}/members`
     * 
     * @param id MAL 俱乐部 ID（必需）
     * @param page 页码，从 1 开始
     * @return 成员列表的响应结果
     */
    suspend fun getClubMembers(
        id: Int,
        page: Int? = null
    ): Result<JikanPageResponse<ClubMember>>
    
    /**
     * 获取俱乐部的工作人员列表。
     * 
     * 路径：`GET /clubs/{id}/staff`
     * 
     * @param id MAL 俱乐部 ID（必需）
     * @return 工作人员列表的响应结果
     */
    suspend fun getClubStaff(id: Int): Result<JikanResponse<List<ClubStaff>>>
    
    /**
     * 获取俱乐部的关系（相关动漫、漫画、角色）。
     * 
     * 路径：`GET /clubs/{id}/relations`
     * 
     * @param id MAL 俱乐部 ID（必需）
     * @return 关系信息的响应结果
     */
    suspend fun getClubRelations(id: Int): Result<JikanResponse<ClubRelations>>
    
    /**
     * 搜索俱乐部。
     * 
     * 路径：`GET /clubs`
     * 
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param type 俱乐部类型：public, private, secret
     * @param category 俱乐部分类：anime, manga, actors_and_artists, characters, cities_and_neighborhoods, companies, conventions, games, japan, music, other, schools
     * @param orderBy 排序字段：mal_id, name, members_count, pictures_count, created
     * @param sort 排序方向：asc, desc
     * @return 搜索结果的响应
     */
    suspend fun searchClubs(
        query: String? = null,
        page: Int? = null,
        limit: Int? = null,
        type: String? = null,
        category: String? = null,
        orderBy: String? = null,
        sort: String? = null
    ): Result<JikanPageResponse<Club>>
}

