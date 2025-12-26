package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.manga.*
import com.pusu.indexed.jikan.models.common.*

/**
 * Top 漫画的过滤条件枚举。
 */
enum class MangaFilter(val value: String) {
    /** 按人气排序 */
    BY_POPULARITY("bypopularity"),
    
    /** 正在连载 */
    PUBLISHING("publishing"),
    
    /** 即将发布 */
    UPCOMING("upcoming"),
    
    /** 按收藏数排序 */
    FAVORITE("favorite")
}

/**
 * Manga 漫画 API。
 * 
 * 提供漫画相关的所有接口，包括：
 * - 基础信息查询（单个、完整）
 * - 角色信息
 * - 新闻、论坛、评论
 * - 图片
 * - 统计数据
 * - 推荐和用户更新
 * - 搜索和排行榜
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/manga
 */
interface MangaApi {
    /**
     * 获取单个漫画的基础信息。
     * 
     * 路径：`GET /manga/{id}`
     * 
     * @param id MAL 漫画 ID（必需）
     * @return 漫画信息的响应结果
     */
    suspend fun getMangaById(id: Int): Result<JikanResponse<Manga>>
    
    /**
     * 获取单个漫画的完整信息（包含关系等）。
     * 
     * 路径：`GET /manga/{id}/full`
     * 
     * @param id MAL 漫画 ID（必需）
     * @return 完整漫画信息的响应结果
     */
    suspend fun getMangaFullById(id: Int): Result<JikanResponse<Manga>>
    
    /**
     * 获取漫画的角色列表。
     * 
     * 路径：`GET /manga/{id}/characters`
     * 
     * @param id MAL 漫画 ID（必需）
     * @return 角色列表的响应结果
     */
    suspend fun getMangaCharacters(id: Int): Result<JikanResponse<List<MangaCharacter>>>
    
    /**
     * 获取漫画的新闻列表。
     * 
     * 路径：`GET /manga/{id}/news`
     * 
     * @param id MAL 漫画 ID（必需）
     * @param page 页码，从 1 开始
     * @return 新闻列表的响应结果
     */
    suspend fun getMangaNews(
        id: Int,
        page: Int? = null
    ): Result<JikanPageResponse<MangaNews>>
    
    /**
     * 获取漫画的论坛话题列表。
     * 
     * 路径：`GET /manga/{id}/forum`
     * 
     * @param id MAL 漫画 ID（必需）
     * @param filter 过滤条件：all, episode, other
     * @return 论坛话题列表的响应结果
     */
    suspend fun getMangaForum(
        id: Int,
        filter: String? = null
    ): Result<JikanResponse<List<com.pusu.indexed.jikan.models.anime.ForumTopic>>>
    
    /**
     * 获取漫画的图片列表。
     * 
     * 路径：`GET /manga/{id}/pictures`
     * 
     * @param id MAL 漫画 ID（必需）
     * @return 图片列表的响应结果
     */
    suspend fun getMangaPictures(id: Int): Result<JikanResponse<List<MangaPicture>>>
    
    /**
     * 获取漫画的统计数据。
     * 
     * 路径：`GET /manga/{id}/statistics`
     * 
     * @param id MAL 漫画 ID（必需）
     * @return 统计数据的响应结果
     */
    suspend fun getMangaStatistics(id: Int): Result<JikanResponse<MangaStatistics>>
    
    /**
     * 获取漫画的更多信息。
     * 
     * 路径：`GET /manga/{id}/moreinfo`
     * 
     * @param id MAL 漫画 ID（必需）
     * @return 更多信息的响应结果
     */
    suspend fun getMangaMoreInfo(id: Int): Result<JikanResponse<String>>
    
    /**
     * 获取漫画的推荐列表。
     * 
     * 路径：`GET /manga/{id}/recommendations`
     * 
     * @param id MAL 漫画 ID（必需）
     * @return 推荐列表的响应结果
     */
    suspend fun getMangaRecommendations(id: Int): Result<JikanResponse<List<Recommendation>>>
    
    /**
     * 获取漫画的用户更新列表。
     * 
     * 路径：`GET /manga/{id}/userupdates`
     * 
     * @param id MAL 漫画 ID（必需）
     * @param page 页码，从 1 开始
     * @return 用户更新列表的响应结果
     */
    suspend fun getMangaUserUpdates(
        id: Int,
        page: Int? = null
    ): Result<JikanPageResponse<UserUpdate>>
    
    /**
     * 获取漫画的评论列表。
     * 
     * 路径：`GET /manga/{id}/reviews`
     * 
     * @param id MAL 漫画 ID（必需）
     * @param page 页码，从 1 开始
     * @return 评论列表的响应结果
     */
    suspend fun getMangaReviews(
        id: Int,
        page: Int? = null
    ): Result<JikanPageResponse<MangaReview>>
    
    /**
     * 搜索漫画。
     * 
     * 路径：`GET /manga`
     * 
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param type 漫画类型：manga, novel, lightnovel, oneshot, doujin, manhwa, manhua
     * @param score 最低评分
     * @param status 状态：publishing, complete, upcoming
     * @param sfw 是否仅显示安全内容
     * @param genres 类型 ID 列表，逗号分隔
     * @param orderBy 排序字段：mal_id, title, start_date, end_date, chapters, volumes, score, scored_by, rank, popularity, members, favorites
     * @param sort 排序方向：asc, desc
     * @return 搜索结果的响应
     */
    suspend fun searchManga(
        query: String? = null,
        page: Int? = null,
        limit: Int? = null,
        type: String? = null,
        score: Double? = null,
        status: String? = null,
        sfw: Boolean? = null,
        genres: String? = null,
        orderBy: String? = null,
        sort: String? = null
    ): Result<JikanPageResponse<Manga>>
    
    /**
     * 获取 Top 漫画排行榜。
     * 
     * 路径：`GET /top/manga`
     * 
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param filter 过滤条件
     * @return 排行榜的响应结果
     */
    suspend fun getTopManga(
        page: Int? = null,
        limit: Int? = null,
        filter: MangaFilter? = null
    ): Result<JikanPageResponse<Manga>>
}

