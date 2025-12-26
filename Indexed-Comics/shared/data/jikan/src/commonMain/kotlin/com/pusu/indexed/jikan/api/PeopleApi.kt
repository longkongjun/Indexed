package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.people.*
import com.pusu.indexed.jikan.models.common.*

/**
 * People 人物 API。
 * 
 * 提供人物（声优/制作人员）相关的所有接口，包括：
 * - 基础信息查询（单个、完整）
 * - 图片
 * - 搜索和排行榜
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/people
 */
interface PeopleApi {
    /**
     * 获取人物的基础信息。
     * 
     * 路径：`GET /people/{id}`
     * 
     * @param id MAL 人物 ID（必需）
     * @return 人物信息的响应结果
     */
    suspend fun getPersonById(id: Int): Result<JikanResponse<Person>>
    
    /**
     * 获取人物的完整信息（包含动漫、漫画、配音）。
     * 
     * 路径：`GET /people/{id}/full`
     * 
     * @param id MAL 人物 ID（必需）
     * @return 完整人物信息的响应结果
     */
    suspend fun getPersonFullById(id: Int): Result<JikanResponse<Person>>
    
    /**
     * 获取人物的图片列表。
     * 
     * 路径：`GET /people/{id}/pictures`
     * 
     * @param id MAL 人物 ID（必需）
     * @return 图片列表的响应结果
     */
    suspend fun getPersonPictures(id: Int): Result<JikanResponse<List<PersonPicture>>>
    
    /**
     * 搜索人物。
     * 
     * 路径：`GET /people`
     * 
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param orderBy 排序字段：mal_id, name, birthday, favorites
     * @param sort 排序方向：asc, desc
     * @return 搜索结果的响应
     */
    suspend fun searchPeople(
        query: String? = null,
        page: Int? = null,
        limit: Int? = null,
        orderBy: String? = null,
        sort: String? = null
    ): Result<JikanPageResponse<Person>>
    
    /**
     * 获取 Top 人物排行榜。
     * 
     * 路径：`GET /top/people`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @return 排行榜的响应结果
     */
    suspend fun getTopPeople(
        page: Int? = null,
        limit: Int? = null
    ): Result<JikanPageResponse<Person>>
}

