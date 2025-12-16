package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.common.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 类型/标签相关 API 接口
 * 提供动漫和漫画类型标签列表查询功能
 */
interface GenresApi {
    
    /**
     * 获取动漫类型列表
     * 
     * @param filter 筛选类型（genres, explicit_genres, themes, demographics）
     * @return 类型列表响应
     */
    @GET("genres/anime")
    suspend fun getAnimeGenres(
        @Query("filter") filter: String? = null
    ): Result<JikanResponse<List<MalUrl>>>
    
    /**
     * 获取漫画类型列表
     * 
     * @param filter 筛选类型（genres, explicit_genres, themes, demographics）
     * @return 类型列表响应
     */
    @GET("genres/manga")
    suspend fun getMangaGenres(
        @Query("filter") filter: String? = null
    ): Result<JikanResponse<List<MalUrl>>>
}

