package com.pusu.indexed.jikan.api

import com.pusu.indexed.jikan.models.user.*
import com.pusu.indexed.jikan.models.common.*

/**
 * User 用户 API。
 * 
 * 提供用户相关的所有接口，包括：
 * - 获取用户基础信息和完整资料
 * - 获取用户统计、收藏、好友
 * - 获取用户历史记录
 * - 搜索用户
 * 
 * 所有路径对齐官方文档：https://docs.api.jikan.moe/#tag/users
 */
interface UserApi {
    /**
     * 获取用户的基础信息。
     * 
     * 路径：`GET /users/{username}`
     * 
     * @param username 用户名（必需）
     * @return 用户信息的响应结果
     */
    suspend fun getUserByUsername(username: String): Result<JikanResponse<User>>
    
    /**
     * 获取用户的完整资料。
     * 
     * 路径：`GET /users/{username}/full`
     * 
     * @param username 用户名（必需）
     * @return 完整用户资料的响应结果
     */
    suspend fun getUserFullProfile(username: String): Result<JikanResponse<User>>
    
    /**
     * 获取用户的统计信息。
     * 
     * 路径：`GET /users/{username}/statistics`
     * 
     * @param username 用户名（必需）
     * @return 用户统计的响应结果
     */
    suspend fun getUserStatistics(username: String): Result<JikanResponse<UserStatistics>>
    
    /**
     * 获取用户的收藏。
     * 
     * 路径：`GET /users/{username}/favorites`
     * 
     * @param username 用户名（必需）
     * @return 用户收藏的响应结果
     */
    suspend fun getUserFavorites(username: String): Result<JikanResponse<UserFavorites>>
    
    /**
     * 获取用户的好友列表。
     * 
     * 路径：`GET /users/{username}/friends`
     * 
     * @param username 用户名（必需）
     * @param page 页码，从 1 开始
     * @return 好友列表的响应结果
     */
    suspend fun getUserFriends(
        username: String,
        page: Int? = null
    ): Result<JikanPageResponse<UserFriend>>
    
    /**
     * 获取用户的历史记录。
     * 
     * 路径：`GET /users/{username}/history`
     * 
     * @param username 用户名（必需）
     * @param type 类型：anime, manga
     * @return 历史记录的响应结果
     */
    suspend fun getUserHistory(
        username: String,
        type: String? = null
    ): Result<JikanResponse<List<UserHistory>>>
    
    /**
     * 搜索用户。
     * 
     * 路径：`GET /users`
     * 
     * @param query 搜索关键词
     * @param page 页码，从 1 开始
     * @param limit 每页数量，最大 25
     * @param gender 性别：any, male, female, nonbinary
     * @param location 所在地
     * @param maxAge 最大年龄
     * @param minAge 最小年龄
     * @return 搜索结果的响应
     */
    suspend fun searchUsers(
        query: String? = null,
        page: Int? = null,
        limit: Int? = null,
        gender: String? = null,
        location: String? = null,
        maxAge: Int? = null,
        minAge: Int? = null
    ): Result<JikanPageResponse<User>>
}

