package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.manga.Manga
import com.pusu.indexed.jikan.models.character.Character
import com.pusu.indexed.jikan.models.people.Person
import com.pusu.indexed.jikan.models.common.*

/**
 * Top 排行榜 API。
 * 
 * 提供各种资源的排行榜接口，包括：
 * - 动漫排行榜
 * - 漫画排行榜
 * - 角色排行榜
 * - 人物排行榜
 * - 评论排行榜
 * 
 * 注意：动漫和漫画排行榜也在各自的 API 中提供，这里是统一入口。
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/top
 */
interface TopApi {
    /**
     * 获取 Top 动漫排行榜。
     * 
     * 路径：`GET /top/anime`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param filter 过滤条件：airing, upcoming, bypopularity, favorite
     * @param type 动漫类型：tv, movie, ova, special, ona, music
     * @return 排行榜的响应结果
     */
    suspend fun getTopAnime(
        page: Int? = null,
        limit: Int? = null,
        filter: String? = null,
        type: String? = null
    ): Result<JikanPageResponse<Anime>>
    
    /**
     * 获取 Top 漫画排行榜。
     * 
     * 路径：`GET /top/manga`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param filter 过滤条件：publishing, upcoming, bypopularity, favorite
     * @param type 漫画类型：manga, novel, lightnovel, oneshot, doujin, manhwa, manhua
     * @return 排行榜的响应结果
     */
    suspend fun getTopManga(
        page: Int? = null,
        limit: Int? = null,
        filter: String? = null,
        type: String? = null
    ): Result<JikanPageResponse<Manga>>
    
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
    
    /**
     * 获取 Top 评论排行榜。
     * 
     * 路径：`GET /top/reviews`
     * 
     * @param page 页码，从 1 开始
     * @return 排行榜的响应结果
     */
    suspend fun getTopReviews(
        page: Int? = null
    ): Result<JikanPageResponse<com.pusu.indexed.jikan.models.review.RecentReview>>
}

