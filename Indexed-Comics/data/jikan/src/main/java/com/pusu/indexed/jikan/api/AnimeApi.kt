package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.*
import com.pusu.indexed.jikan.models.common.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 动漫相关 API 接口
 * 提供动漫信息查询、搜索等功能
 */
interface AnimeApi {
    
    /**
     * 获取动漫基本信息
     * 
     * @param id 动漫的 MAL ID
     * @return 动漫信息响应
     */
    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") id: Int
    ): Result<JikanResponse<Anime>>
    
    /**
     * 获取动漫完整信息
     * 包含所有可用的动漫数据
     * 
     * @param id 动漫的 MAL ID
     * @return 动漫完整信息响应
     */
    @GET("anime/{id}/full")
    suspend fun getAnimeFullById(
        @Path("id") id: Int
    ): Result<JikanResponse<Anime>>
    
    /**
     * 获取动漫角色和声优信息
     * 
     * @param id 动漫的 MAL ID
     * @return 角色列表响应
     */
    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(
        @Path("id") id: Int
    ): Result<JikanResponse<List<AnimeCharacter>>>
    
    /**
     * 获取动漫工作人员信息
     * 
     * @param id 动漫的 MAL ID
     * @return 工作人员列表响应
     */
    @GET("anime/{id}/staff")
    suspend fun getAnimeStaff(
        @Path("id") id: Int
    ): Result<JikanResponse<List<AnimeStaff>>>
    
    /**
     * 获取动漫剧集列表
     * 
     * @param id 动漫的 MAL ID
     * @param page 页码（可选）
     * @return 剧集列表分页响应
     */
    @GET("anime/{id}/episodes")
    suspend fun getAnimeEpisodes(
        @Path("id") id: Int,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<AnimeEpisode>>
    
    /**
     * 获取动漫特定剧集信息
     * 
     * @param id 动漫的 MAL ID
     * @param episode 集数
     * @return 剧集信息响应
     */
    @GET("anime/{id}/episodes/{episode}")
    suspend fun getAnimeEpisodeById(
        @Path("id") id: Int,
        @Path("episode") episode: Int
    ): Result<JikanResponse<AnimeEpisode>>
    
    /**
     * 获取动漫新闻列表
     * 
     * @param id 动漫的 MAL ID
     * @param page 页码（可选）
     * @return 新闻列表分页响应
     */
    @GET("anime/{id}/news")
    suspend fun getAnimeNews(
        @Path("id") id: Int,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<AnimeNews>>
    
    /**
     * 获取动漫论坛话题
     * 
     * @param id 动漫的 MAL ID
     * @param filter 话题类型筛选（all, episode, other）
     * @return 论坛话题列表响应
     */
    @GET("anime/{id}/forum")
    suspend fun getAnimeForum(
        @Path("id") id: Int,
        @Query("filter") filter: String? = null
    ): Result<JikanResponse<List<ForumTopic>>>
    
    /**
     * 获取动漫视频
     * 包含宣传视频、剧集视频、音乐视频
     * 
     * @param id 动漫的 MAL ID
     * @return 视频集合响应
     */
    @GET("anime/{id}/videos")
    suspend fun getAnimeVideos(
        @Path("id") id: Int
    ): Result<JikanResponse<AnimeVideos>>
    
    /**
     * 获取动漫图片集
     * 
     * @param id 动漫的 MAL ID
     * @return 图片列表响应
     */
    @GET("anime/{id}/pictures")
    suspend fun getAnimePictures(
        @Path("id") id: Int
    ): Result<JikanResponse<List<Picture>>>
    
    /**
     * 获取动漫统计信息
     * 包含观看人数、评分分布等统计数据
     * 
     * @param id 动漫的 MAL ID
     * @return 统计信息响应
     */
    @GET("anime/{id}/statistics")
    suspend fun getAnimeStatistics(
        @Path("id") id: Int
    ): Result<JikanResponse<AnimeStatistics>>
    
    /**
     * 获取动漫更多信息
     * 
     * @param id 动漫的 MAL ID
     * @return 更多信息响应
     */
    @GET("anime/{id}/moreinfo")
    suspend fun getAnimeMoreInfo(
        @Path("id") id: Int
    ): Result<JikanResponse<String>>
    
    /**
     * 获取动漫推荐列表
     * 
     * @param id 动漫的 MAL ID
     * @return 推荐列表响应
     */
    @GET("anime/{id}/recommendations")
    suspend fun getAnimeRecommendations(
        @Path("id") id: Int
    ): Result<JikanResponse<List<Recommendation>>>
    
    /**
     * 获取动漫用户更新
     * 
     * @param id 动漫的 MAL ID
     * @param page 页码（可选）
     * @return 用户更新列表分页响应
     */
    @GET("anime/{id}/userupdates")
    suspend fun getAnimeUserUpdates(
        @Path("id") id: Int,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<UserUpdate>>
    
    /**
     * 获取动漫评论列表
     * 
     * @param id 动漫的 MAL ID
     * @param page 页码（可选）
     * @param preliminary 是否包含初步评论（可选）
     * @param spoiler 是否包含剧透评论（可选）
     * @return 评论列表分页响应
     */
    @GET("anime/{id}/reviews")
    suspend fun getAnimeReviews(
        @Path("id") id: Int,
        @Query("page") page: Int? = null,
        @Query("preliminary") preliminary: Boolean? = null,
        @Query("spoiler") spoiler: Boolean? = null
    ): Result<JikanPageResponse<AnimeReview>>
    
    /**
     * 获取动漫关系
     * 包含续集、前传、衍生作品等相关作品信息
     * 
     * @param id 动漫的 MAL ID
     * @return 关系列表响应
     */
    @GET("anime/{id}/relations")
    suspend fun getAnimeRelations(
        @Path("id") id: Int
    ): Result<JikanResponse<List<Relation>>>
    
    /**
     * 获取动漫主题曲
     * 包含片头曲和片尾曲列表
     * 
     * @param id 动漫的 MAL ID
     * @return 主题曲信息响应
     */
    @GET("anime/{id}/themes")
    suspend fun getAnimeThemes(
        @Path("id") id: Int
    ): Result<JikanResponse<AnimeTheme>>
    
    /**
     * 获取动漫外部链接
     * 
     * @param id 动漫的 MAL ID
     * @return 外部链接列表响应
     */
    @GET("anime/{id}/external")
    suspend fun getAnimeExternal(
        @Path("id") id: Int
    ): Result<JikanResponse<List<ExternalLink>>>
    
    /**
     * 获取动漫流媒体链接
     * 
     * @param id 动漫的 MAL ID
     * @return 流媒体链接列表响应
     */
    @GET("anime/{id}/streaming")
    suspend fun getAnimeStreaming(
        @Path("id") id: Int
    ): Result<JikanResponse<List<ExternalLink>>>
    
    /**
     * 搜索动漫
     * 
     * @param query 搜索关键词（可选）
     * @param type 动漫类型（tv, movie, ova, special, ona, music）
     * @param score 最低评分（可选）
     * @param minScore 最低评分（可选）
     * @param maxScore 最高评分（可选）
     * @param status 播放状态（airing, complete, upcoming）
     * @param rating 年龄分级（g, pg, pg13, r17, r, rx）
     * @param sfw 是否仅显示安全内容（可选）
     * @param genres 类型 ID（逗号分隔）
     * @param orderBy 排序字段（mal_id, title, type, rating, start_date, end_date, episodes, score, scored_by, rank, popularity, members, favorites）
     * @param sort 排序方向（asc, desc）
     * @param letter 首字母筛选（可选）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @return 动漫列表分页响应
     */
    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String? = null,
        @Query("type") type: String? = null,
        @Query("score") score: Double? = null,
        @Query("min_score") minScore: Double? = null,
        @Query("max_score") maxScore: Double? = null,
        @Query("status") status: String? = null,
        @Query("rating") rating: String? = null,
        @Query("sfw") sfw: Boolean? = null,
        @Query("genres") genres: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("letter") letter: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): JikanPageResponse<Anime>
}

