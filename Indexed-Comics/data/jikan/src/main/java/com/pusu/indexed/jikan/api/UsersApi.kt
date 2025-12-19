package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.anime.AnimeReview
import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.user.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 用户相关 API 接口
 * 提供用户信息查询功能
 */
interface UsersApi {
    
    /**
     * 获取用户基本信息
     * 
     * @param username 用户名
     * @return 用户信息响应
     */
    @GET("users/{username}")
    suspend fun getUserByUsername(
        @Path("username") username: String
    ): Result<JikanResponse<User>>
    
    /**
     * 获取用户完整信息
     * 
     * @param username 用户名
     * @return 用户完整信息响应
     */
    @GET("users/{username}/full")
    suspend fun getUserFullByUsername(
        @Path("username") username: String
    ): Result<JikanResponse<User>>
    
    /**
     * 获取用户统计信息
     * 
     * @param username 用户名
     * @return 统计信息响应
     */
    @GET("users/{username}/statistics")
    suspend fun getUserStatistics(
        @Path("username") username: String
    ): Result<JikanResponse<UserStatistics>>
    
    /**
     * 获取用户收藏
     * 
     * @param username 用户名
     * @return 收藏信息响应
     */
    @GET("users/{username}/favorites")
    suspend fun getUserFavorites(
        @Path("username") username: String
    ): Result<JikanResponse<UserFavorites>>
    
    /**
     * 获取用户更新
     * 
     * @param username 用户名
     * @return 更新信息响应
     */
    @GET("users/{username}/updates")
    suspend fun getUserUpdates(
        @Path("username") username: String
    ): Result<JikanResponse<UserUpdate>>
    
    /**
     * 获取用户关于
     * 
     * @param username 用户名
     * @return 关于信息响应
     */
    @GET("users/{username}/about")
    suspend fun getUserAbout(
        @Path("username") username: String
    ): Result<JikanResponse<String>>
    
    /**
     * 获取用户评论
     * 
     * @param username 用户名
     * @param page 页码（可选）
     * @return 评论列表分页响应
     */
    @GET("users/{username}/reviews")
    suspend fun getUserReviews(
        @Path("username") username: String,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<AnimeReview>>
    
    /**
     * 获取用户推荐
     * 
     * @param username 用户名
     * @return 推荐列表响应
     */
    @GET("users/{username}/recommendations")
    suspend fun getUserRecommendations(
        @Path("username") username: String
    ): Result<JikanResponse<AnimeReview>>
    
    /**
     * 获取用户俱乐部
     * 
     * @param username 用户名
     * @param page 页码（可选）
     * @return 俱乐部列表分页响应
     */
    @GET("users/{username}/clubs")
    suspend fun getUserClubs(
        @Path("username") username: String,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<User>>
    
    /**
     * 获取用户外部链接
     * 
     * @param username 用户名
     * @return 外部链接列表响应
     */
    @GET("users/{username}/external")
    suspend fun getUserExternal(
        @Path("username") username: String
    ): Result<JikanResponse<List<Recommendation>>>
    
    /**
     * 获取用户好友
     * 
     * @param username 用户名
     * @param page 页码（可选）
     * @return 好友列表分页响应
     */
    @GET("users/{username}/friends")
    suspend fun getUserFriends(
        @Path("username") username: String,
        @Query("page") page: Int? = null
    ): Result<JikanPageResponse<UserStatistics>>
    
    /**
     * 获取用户历史
     * 
     * @param username 用户名
     * @param type 类型（anime, manga）
     * @return 历史列表响应
     */
    @GET("users/{username}/history")
    suspend fun getUserHistory(
        @Path("username") username: String,
        @Query("type") type: String? = null
    ): Result<JikanResponse<MalUrl>>
    
    /**
     * 搜索用户
     * 
     * @param query 搜索关键词（可选）
     * @param page 页码（可选）
     * @param limit 每页数量（可选）
     * @return 用户列表分页响应
     */
    @GET("users")
    suspend fun searchUsers(
        @Query("q") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<JikanPageResponse<UserFavorites>>
}

