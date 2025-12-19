package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.manga.*
import com.pusu.indexed.jikan.models.anime.ForumTopic
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 漫画相关 API 接口
 * 提供漫画信息查询、搜索等功能
 */
interface MangaApi {
    
    /**
     * 获取漫画基本信息
     * 
     * @param id 漫画的 MAL ID
     * @return 漫画信息响应
     */
    @GET("manga/{id}")
    suspend fun getMangaById(
        @Path("id") id: Int
    ): Result<JikanResponse<Manga>>
    
    /**
     * 获取漫画完整信息
     * 包含所有可用的漫画数据
     * 
     * @param id 漫画的 MAL ID
     * @return 漫画完整信息响应
     */
    @GET("manga/{id}/full")
    suspend fun getMangaFullById(
        @Path("id") id: Int
    ): Result<JikanResponse<Manga>>
    
    /**
     * 获取漫画角色信息
     * 
     * @param id 漫画的 MAL ID
     * @return 角色列表响应
     */
    @GET("manga/{id}/characters")
    suspend fun getMangaCharacters(
        @Path("id") id: Int
    ): Result<JikanResponse<List<MangaCharacter>>>
    
    /**
     * 获取漫画新闻列表
     * 
     * @param id 漫画的 MAL ID
     * @param page 页码（可选）
     * @return 新闻列表分页响应
     */
    @GET("manga/{id}/news")
    suspend fun getMangaNews(
        @Path("id") id: Int,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<MangaNews>>
    
    /**
     * 获取漫画论坛话题
     * 
     * @param id 漫画的 MAL ID
     * @param filter 话题类型筛选（all, episode, other）
     * @return 论坛话题列表响应
     */
    @GET("manga/{id}/forum")
    suspend fun getMangaForum(
        @Path("id") id: Int,
        @Query("filter") filter: String? = null
    ): Result<JikanResponse<List<ForumTopic>>>
    
    /**
     * 获取漫画图片集
     * 
     * @param id 漫画的 MAL ID
     * @return 图片列表响应
     */
    @GET("manga/{id}/pictures")
    suspend fun getMangaPictures(
        @Path("id") id: Int
    ): Result<JikanResponse<List<ForumTopic>>>
    
    /**
     * 获取漫画统计信息
     * 包含阅读人数、评分分布等统计数据
     * 
     * @param id 漫画的 MAL ID
     * @return 统计信息响应
     */
    @GET("manga/{id}/statistics")
    suspend fun getMangaStatistics(
        @Path("id") id: Int
    ): Result<JikanResponse<List<MangaPicture>>>
    
    /**
     * 获取漫画更多信息
     * 
     * @param id 漫画的 MAL ID
     * @return 更多信息响应
     */
    @GET("manga/{id}/moreinfo")
    suspend fun getMangaMoreInfo(
        @Path("id") id: Int
    ): Result<JikanResponse<MangaStatistics>>
    
    /**
     * 获取漫画推荐列表
     * 
     * @param id 漫画的 MAL ID
     * @return 推荐列表响应
     */
    @GET("manga/{id}/recommendations")
    suspend fun getMangaRecommendations(
        @Path("id") id: Int
    ): Result<JikanResponse<String>>
    
    /**
     * 获取漫画用户更新
     * 
     * @param id 漫画的 MAL ID
     * @param page 页码（可选）
     * @return 用户更新列表分页响应
     */
    @GET("manga/{id}/userupdates")
    suspend fun getMangaUserUpdates(
        @Path("id") id: Int,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<Manga>>
    
    /**
     * 获取漫画评论列表
     * 
     * @param id 漫画的 MAL ID
     * @param page 页码（可选）
     * @param preliminary 是否包含初步评论（可选）
     * @param spoiler 是否包含剧透评论（可选）
     * @return 评论列表分页响应
     */
    @GET("manga/{id}/reviews")
    suspend fun getMangaReviews(
        @Path("id") id: Int,
        @Query("page") page: Int? = null,
        @Query("preliminary") preliminary: Boolean? = null,
        @Query("spoiler") spoiler: Boolean? = null
    ): Result<JikanPageResponse<List<MangaCharacter>>>
    
    /**
     * 获取漫画关系
     * 包含续集、前传、衍生作品等相关作品信息
     * 
     * @param id 漫画的 MAL ID
     * @return 关系列表响应
     */
    @GET("manga/{id}/relations")
    suspend fun getMangaRelations(
        @Path("id") id: Int
    ): Result<JikanResponse<List<Recommendation>>>
    
    /**
     * 获取漫画外部链接
     * 
     * @param id 漫画的 MAL ID
     * @return 外部链接列表响应
     */
    @GET("manga/{id}/external")
    suspend fun getMangaExternal(
        @Path("id") id: Int
    ): Result<JikanResponse<UserUpdate>>
    
    /**
     * 搜索漫画
     * 
     * @param query 搜索关键词（可选）
     * @param type 漫画类型（manga, novel, lightnovel, oneshot, doujin, manhwa, manhua）
     * @param score 最低评分（可选）
     * @param minScore 最低评分（可选）
     * @param maxScore 最高评分（可选）
     * @param status 连载状态（publishing, complete, upcoming, discontinued, hiatus）
     * @param sfw 是否仅显示安全内容（可选）
     * @param genres 类型 ID（逗号分隔）
     * @param orderBy 排序字段（mal_id, title, type, start_date, end_date, chapters, volumes, score, scored_by, rank, popularity, members, favorites）
     * @param sort 排序方向（asc, desc）
     * @param letter 首字母筛选（可选）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @return 漫画列表分页响应
     */
    @GET("manga")
    suspend fun searchManga(
        @Query("q") query: String? = null,
        @Query("type") type: String? = null,
        @Query("score") score: Double? = null,
        @Query("min_score") minScore: Double? = null,
        @Query("max_score") maxScore: Double? = null,
        @Query("status") status: String? = null,
        @Query("sfw") sfw: Boolean? = null,
        @Query("genres") genres: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("letter") letter: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<JikanPageResponse<MangaNews>>
}

