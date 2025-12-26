package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.*
import com.pusu.indexed.jikan.models.common.*

/**
 * Top 动漫的过滤条件枚举。
 */
enum class AnimeFilter(val value: String) {
    /** 按人气排序 */
    BY_POPULARITY("bypopularity"),

    /** 正在播出 */
    AIRING("airing"),

    /** 即将播出 */
    UPCOMING("upcoming"),

    /** 按收藏数排序 */
    FAVORITE("favorite")
}

/**
 * Anime 动漫 API。
 *
 * 提供动漫相关的所有接口，包括：
 * - 基础信息查询（单个、完整）
 * - 角色、工作人员信息
 * - 剧集列表和详情
 * - 新闻、论坛、评论
 * - 视频、图片
 * - 统计数据
 * - 推荐和用户更新
 * - 搜索和排行榜
 *
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/anime
 */
interface AnimeApi {
    /**
     * 获取单个动漫的基础信息。
     *
     * 路径：`GET /anime/{id}`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 动漫信息的响应结果
     */
    suspend fun getAnimeById(id: Int): Result<JikanResponse<Anime>>

    /**
     * 获取单个动漫的完整信息（包含关系、主题曲等）。
     *
     * 路径：`GET /anime/{id}/full`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 完整动漫信息的响应结果
     */
    suspend fun getAnimeFullById(id: Int): Result<JikanResponse<Anime>>

    /**
     * 获取动漫的角色列表。
     *
     * 路径：`GET /anime/{id}/characters`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 角色列表的响应结果
     */
    suspend fun getAnimeCharacters(id: Int): Result<JikanResponse<List<AnimeCharacter>>>

    /**
     * 获取动漫的工作人员列表。
     *
     * 路径：`GET /anime/{id}/staff`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 工作人员列表的响应结果
     */
    suspend fun getAnimeStaff(id: Int): Result<JikanResponse<List<AnimeStaff>>>

    /**
     * 获取动漫的剧集列表。
     *
     * 路径：`GET /anime/{id}/episodes`
     *
     * @param id MAL 动漫 ID（必需）
     * @param page 页码，从 1 开始
     * @return 剧集列表的响应结果
     */
    suspend fun getAnimeEpisodes(
        id: Int,
        page: Int? = null,
    ): Result<JikanPageResponse<AnimeEpisode>>

    /**
     * 获取动漫的单个剧集信息。
     *
     * 路径：`GET /anime/{id}/episodes/{episode}`
     *
     * @param id MAL 动漫 ID（必需）
     * @param episode 集数（必需）
     * @return 剧集信息的响应结果
     */
    suspend fun getAnimeEpisodeById(
        id: Int,
        episode: Int,
    ): Result<JikanResponse<AnimeEpisode>>

    /**
     * 获取动漫的新闻列表。
     *
     * 路径：`GET /anime/{id}/news`
     *
     * @param id MAL 动漫 ID（必需）
     * @param page 页码，从 1 开始
     * @return 新闻列表的响应结果
     */
    suspend fun getAnimeNews(
        id: Int,
        page: Int? = null,
    ): Result<JikanPageResponse<AnimeNews>>

    /**
     * 获取动漫的论坛话题列表。
     *
     * 路径：`GET /anime/{id}/forum`
     *
     * @param id MAL 动漫 ID（必需）
     * @param filter 过滤条件：all, episode, other
     * @return 论坛话题列表的响应结果
     */
    suspend fun getAnimeForum(
        id: Int,
        filter: String? = null,
    ): Result<JikanResponse<List<ForumTopic>>>

    /**
     * 获取动漫的视频列表（预告、剧集、MV）。
     *
     * 路径：`GET /anime/{id}/videos`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 视频集合的响应结果
     */
    suspend fun getAnimeVideos(id: Int): Result<JikanResponse<AnimeVideos>>

    /**
     * 获取动漫的图片列表。
     *
     * 路径：`GET /anime/{id}/pictures`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 图片列表的响应结果
     */
    suspend fun getAnimePictures(id: Int): Result<JikanResponse<List<Picture>>>

    /**
     * 获取动漫的统计数据。
     *
     * 路径：`GET /anime/{id}/statistics`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 统计数据的响应结果
     */
    suspend fun getAnimeStatistics(id: Int): Result<JikanResponse<AnimeStatistics>>

    /**
     * 获取动漫的更多信息。
     *
     * 路径：`GET /anime/{id}/moreinfo`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 更多信息的响应结果
     */
    suspend fun getAnimeMoreInfo(id: Int): Result<JikanResponse<String>>

    /**
     * 获取动漫的推荐列表。
     *
     * 路径：`GET /anime/{id}/recommendations`
     *
     * @param id MAL 动漫 ID（必需）
     * @return 推荐列表的响应结果
     */
    suspend fun getAnimeRecommendations(id: Int): Result<JikanResponse<List<Recommendation>>>

    /**
     * 获取动漫的用户更新列表。
     *
     * 路径：`GET /anime/{id}/userupdates`
     *
     * @param id MAL 动漫 ID（必需）
     * @param page 页码，从 1 开始
     * @return 用户更新列表的响应结果
     */
    suspend fun getAnimeUserUpdates(
        id: Int,
        page: Int? = null,
    ): Result<JikanPageResponse<UserUpdate>>

    /**
     * 获取动漫的评论列表。
     *
     * 路径：`GET /anime/{id}/reviews`
     *
     * @param id MAL 动漫 ID（必需）
     * @param page 页码，从 1 开始
     * @return 评论列表的响应结果
     */
    suspend fun getAnimeReviews(
        id: Int,
        page: Int? = null,
    ): Result<JikanPageResponse<AnimeReview>>

    /**
     * 搜索动漫。
     *
     * 路径：`GET /anime`
     *
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param type 动漫类型：tv, movie, ova, special, ona, music
     * @param score 最低评分
     * @param status 状态：airing, complete, upcoming
     * @param rating 分级：g, pg, pg13, r17, r, rx
     * @param sfw 是否仅显示安全内容
     * @param genres 类型 ID 列表，逗号分隔
     * @param orderBy 排序字段：mal_id, title, start_date, end_date, episodes, score, scored_by, rank, popularity, members, favorites
     * @param sort 排序方向：asc, desc
     * @return 搜索结果的响应
     */
    suspend fun searchAnime(
        query: String? = null,
        page: Int? = null,
        limit: Int? = null,
        type: String? = null,
        score: Double? = null,
        status: String? = null,
        rating: String? = null,
        sfw: Boolean? = null,
        genres: String? = null,
        orderBy: String? = null,
        sort: String? = null,
    ): Result<JikanPageResponse<Anime>>

    /**
     * 获取 Top 动漫排行榜。
     *
     * 路径：`GET /top/anime`
     *
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param filter 过滤条件
     * @return 排行榜的响应结果
     */
    suspend fun getTopAnime(
        page: Int? = null,
        limit: Int? = null,
        filter: AnimeFilter? = null,
    ): Result<JikanPageResponse<Anime>>
}

