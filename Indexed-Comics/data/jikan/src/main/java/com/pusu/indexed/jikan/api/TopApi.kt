package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.anime.AnimeReview
import com.pusu.indexed.jikan.models.character.Character
import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.manga.Manga
import com.pusu.indexed.jikan.models.people.Person
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 排行榜相关 API 接口
 * 提供各类排行榜查询功能
 */
interface TopApi {
    
    /**
     * 获取动漫排行榜
     * 
     * @param type 类型筛选（tv, movie, ova, special, ona, music）
     * @param filter 排序方式（airing, upcoming, bypopularity, favorite）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @return 动漫列表分页响应
     */
    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("type") type: String? = null,
        @Query("filter") filter: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<JikanPageResponse<Anime>>
    
    /**
     * 获取漫画排行榜
     * 
     * @param type 类型筛选（manga, novel, lightnovel, oneshot, doujin, manhwa, manhua）
     * @param filter 排序方式（publishing, upcoming, bypopularity, favorite）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @return 漫画列表分页响应
     */
    @GET("top/manga")
    suspend fun getTopManga(
        @Query("type") type: String? = null,
        @Query("filter") filter: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<JikanPageResponse<Manga>>
    
    /**
     * 获取角色排行榜
     * 
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @return 角色列表分页响应
     */
    @GET("top/characters")
    suspend fun getTopCharacters(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<JikanPageResponse<Character>>
    
    /**
     * 获取人物排行榜
     * 
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @return 人物列表分页响应
     */
    @GET("top/people")
    suspend fun getTopPeople(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<JikanPageResponse<Person>>
    
    /**
     * 获取评论排行榜
     * 
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @return 评论列表分页响应
     */
    @GET("top/reviews")
    suspend fun getTopReviews(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<JikanPageResponse<AnimeReview>>
}

