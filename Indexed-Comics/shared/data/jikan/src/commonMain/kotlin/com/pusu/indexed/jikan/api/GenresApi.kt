package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.genre.Genre
import com.pusu.indexed.jikan.models.common.*

/**
 * Genres 类型/题材 API。
 * 
 * 提供类型和题材相关的接口，包括：
 * - 获取动漫类型列表
 * - 获取漫画类型列表
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/genres
 */
interface GenresApi {
    /**
     * 获取动漫类型列表。
     * 
     * 路径：`GET /genres/anime`
     * 
     * @param filter 过滤条件：genres, explicit_genres, themes, demographics
     * @return 类型列表的响应结果
     */
    suspend fun getAnimeGenres(
        filter: String? = null
    ): Result<JikanResponse<List<Genre>>>
    
    /**
     * 获取漫画类型列表。
     * 
     * 路径：`GET /genres/manga`
     * 
     * @param filter 过滤条件：genres, explicit_genres, themes, demographics
     * @return 类型列表的响应结果
     */
    suspend fun getMangaGenres(
        filter: String? = null
    ): Result<JikanResponse<List<Genre>>>
}

