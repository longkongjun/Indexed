package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.season.Season
import com.pusu.indexed.jikan.models.common.*

/**
 * Season 季度 API。
 * 
 * 提供季度相关的所有接口，包括：
 * - 获取季度动漫列表
 * - 获取当前季度
 * - 获取即将播出的季度
 * - 获取所有可用季度列表
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/seasons
 */
interface SeasonApi {
    /**
     * 获取指定季度的动漫列表。
     * 
     * 路径：`GET /seasons/{year}/{season}`
     * 
     * @param year 年份，例如 2023（必需）
     * @param season 季度：winter, spring, summer, fall（必需）
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @return 季度动漫列表的响应结果
     */
    suspend fun getSeasonAnime(
        year: Int,
        season: String,
        page: Int? = null,
        limit: Int? = null
    ): Result<JikanPageResponse<Anime>>
    
    /**
     * 获取当前季度的动漫列表。
     * 
     * 路径：`GET /seasons/now`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @return 当前季度动漫列表的响应结果
     */
    suspend fun getCurrentSeasonAnime(
        page: Int? = null,
        limit: Int? = null
    ): Result<JikanPageResponse<Anime>>
    
    /**
     * 获取即将播出的季度动漫列表。
     * 
     * 路径：`GET /seasons/upcoming`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @return 即将播出动漫列表的响应结果
     */
    suspend fun getUpcomingSeasonAnime(
        page: Int? = null,
        limit: Int? = null
    ): Result<JikanPageResponse<Anime>>
    
    /**
     * 获取所有可用的季度列表。
     * 
     * 路径：`GET /seasons`
     * 
     * @return 所有季度列表的响应结果
     */
    suspend fun getAllSeasons(): Result<JikanResponse<List<Season>>>
}

