package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.character.*
import com.pusu.indexed.jikan.models.common.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 角色相关 API 接口
 * 提供角色信息查询、搜索等功能
 */
interface CharactersApi {
    
    /**
     * 获取角色基本信息
     * 
     * @param id 角色的 MAL ID
     * @return 角色信息响应
     */
    @GET("characters/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Result<JikanResponse<Character>>
    
    /**
     * 获取角色完整信息
     * 包含所有可用的角色数据
     * 
     * @param id 角色的 MAL ID
     * @return 角色完整信息响应
     */
    @GET("characters/{id}/full")
    suspend fun getCharacterFullById(
        @Path("id") id: Int
    ): Result<JikanResponse<Character>>
    
    /**
     * 获取角色动漫出演列表
     * 
     * @param id 角色的 MAL ID
     * @return 动漫列表响应
     */
    @GET("characters/{id}/anime")
    suspend fun getCharacterAnime(
        @Path("id") id: Int
    ): Result<JikanResponse<List<CharacterAnimeEntry>>>
    
    /**
     * 获取角色漫画出现列表
     * 
     * @param id 角色的 MAL ID
     * @return 漫画列表响应
     */
    @GET("characters/{id}/manga")
    suspend fun getCharacterManga(
        @Path("id") id: Int
    ): Result<JikanResponse<List<CharacterMangaEntry>>>
    
    /**
     * 获取角色声优列表
     * 
     * @param id 角色的 MAL ID
     * @return 声优列表响应
     */
    @GET("characters/{id}/voices")
    suspend fun getCharacterVoices(
        @Path("id") id: Int
    ): Result<JikanResponse<List<CharacterVoiceActor>>>
    
    /**
     * 获取角色图片集
     * 
     * @param id 角色的 MAL ID
     * @return 图片列表响应
     */
    @GET("characters/{id}/pictures")
    suspend fun getCharacterPictures(
        @Path("id") id: Int
    ): Result<JikanResponse<List<CharacterPicture>>>
    
    /**
     * 搜索角色
     * 
     * @param query 搜索关键词（可选）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param orderBy 排序字段（mal_id, name, favorites）
     * @param sort 排序方向（asc, desc）
     * @param letter 首字母筛选（可选）
     * @return 角色列表分页响应
     */
    @GET("characters")
    suspend fun searchCharacters(
        @Query("q") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("letter") letter: String? = null
    ): Result<JikanPageResponse<Character>>
}

