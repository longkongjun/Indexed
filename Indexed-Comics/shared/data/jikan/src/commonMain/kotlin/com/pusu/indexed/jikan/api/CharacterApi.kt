package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.character.*
import com.pusu.indexed.jikan.models.common.*

/**
 * Character 角色 API。
 * 
 * 提供角色相关的所有接口，包括：
 * - 基础信息查询（单个、完整）
 * - 图片
 * - 搜索和排行榜
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/characters
 */
interface CharacterApi {
    /**
     * 获取角色的基础信息。
     * 
     * 路径：`GET /characters/{id}`
     * 
     * @param id MAL 角色 ID（必需）
     * @return 角色信息的响应结果
     */
    suspend fun getCharacterById(id: Int): Result<JikanResponse<Character>>
    
    /**
     * 获取角色的完整信息（包含动漫、漫画、声优）。
     * 
     * 路径：`GET /characters/{id}/full`
     * 
     * @param id MAL 角色 ID（必需）
     * @return 完整角色信息的响应结果
     */
    suspend fun getCharacterFullById(id: Int): Result<JikanResponse<Character>>
    
    /**
     * 获取角色的图片列表。
     * 
     * 路径：`GET /characters/{id}/pictures`
     * 
     * @param id MAL 角色 ID（必需）
     * @return 图片列表的响应结果
     */
    suspend fun getCharacterPictures(id: Int): Result<JikanResponse<List<CharacterPicture>>>
    
    /**
     * 搜索角色。
     * 
     * 路径：`GET /characters`
     * 
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param orderBy 排序字段：mal_id, name, favorites
     * @param sort 排序方向：asc, desc
     * @return 搜索结果的响应
     */
    suspend fun searchCharacters(
        query: String? = null,
        page: Int? = null,
        limit: Int? = null,
        orderBy: String? = null,
        sort: String? = null
    ): Result<JikanPageResponse<Character>>
    
    /**
     * 获取 Top 角色排行榜。
     * 
     * 路径：`GET /top/characters`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @return 排行榜的响应结果
     */
    suspend fun getTopCharacters(
        page: Int? = null,
        limit: Int? = null
    ): Result<JikanPageResponse<Character>>
}

