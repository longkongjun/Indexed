package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.people.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 人物相关 API 接口
 * 提供人物（声优/制作人员）信息查询、搜索等功能
 */
interface PeopleApi {
    
    /**
     * 获取人物基本信息
     * 
     * @param id 人物的 MAL ID
     * @return 人物信息响应
     */
    @GET("people/{id}")
    suspend fun getPersonById(
        @Path("id") id: Int
    ): Result<JikanResponse<Person>>
    
    /**
     * 获取人物完整信息
     * 包含所有可用的人物数据
     * 
     * @param id 人物的 MAL ID
     * @return 人物完整信息响应
     */
    @GET("people/{id}/full")
    suspend fun getPersonFullById(
        @Path("id") id: Int
    ): Result<JikanResponse<Person>>
    
    /**
     * 获取人物动漫作品列表
     * 
     * @param id 人物的 MAL ID
     * @return 动漫作品列表响应
     */
    @GET("people/{id}/anime")
    suspend fun getPersonAnime(
        @Path("id") id: Int
    ): Result<JikanResponse<List<PersonAnimeEntry>>>
    
    /**
     * 获取人物配音角色列表
     * 
     * @param id 人物的 MAL ID
     * @return 配音角色列表响应
     */
    @GET("people/{id}/voices")
    suspend fun getPersonVoices(
        @Path("id") id: Int
    ): Result<JikanResponse<List<PersonVoiceRole>>>
    
    /**
     * 获取人物漫画作品列表
     * 
     * @param id 人物的 MAL ID
     * @return 漫画作品列表响应
     */
    @GET("people/{id}/manga")
    suspend fun getPersonManga(
        @Path("id") id: Int
    ): Result<JikanResponse<List<PersonMangaEntry>>>
    
    /**
     * 获取人物图片集
     * 
     * @param id 人物的 MAL ID
     * @return 图片列表响应
     */
    @GET("people/{id}/pictures")
    suspend fun getPersonPictures(
        @Path("id") id: Int
    ): Result<JikanResponse<List<PersonPicture>>>
    
    /**
     * 搜索人物
     * 
     * @param query 搜索关键词（可选）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @param orderBy 排序字段（mal_id, name, birthday, favorites）
     * @param sort 排序方向（asc, desc）
     * @param letter 首字母筛选（可选）
     * @return 人物列表分页响应
     */
    @GET("people")
    suspend fun searchPeople(
        @Query("q") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("letter") letter: String? = null
    ): Result<JikanPageResponse<Person>>
}

