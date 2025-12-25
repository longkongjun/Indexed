package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.UserApi
import com.pusu.indexed.jikan.models.user.*
import com.pusu.indexed.jikan.models.common.*

/**
 * UserApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class UserApiImpl(
    private val client: JikanClient
) : UserApi {
    
    override suspend fun getUserByUsername(username: String): Result<JikanResponse<User>> =
        client.get(path = "users/$username")
    
    override suspend fun getUserFullProfile(username: String): Result<JikanResponse<User>> =
        client.get(path = "users/$username/full")
    
    override suspend fun getUserStatistics(username: String): Result<JikanResponse<UserStatistics>> =
        client.get(path = "users/$username/statistics")
    
    override suspend fun getUserFavorites(username: String): Result<JikanResponse<UserFavorites>> =
        client.get(path = "users/$username/favorites")
    
    override suspend fun getUserFriends(
        username: String,
        page: Int?
    ): Result<JikanPageResponse<UserFriend>> =
        client.get(
            path = "users/$username/friends",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
    
    override suspend fun getUserHistory(
        username: String,
        type: String?
    ): Result<JikanResponse<List<UserHistory>>> =
        client.get(
            path = "users/$username/history",
            query = buildMap {
                type?.let { put("type", it) }
            }
        )
    
    override suspend fun searchUsers(
        query: String?,
        page: Int?,
        limit: Int?,
        gender: String?,
        location: String?,
        maxAge: Int?,
        minAge: Int?
    ): Result<JikanPageResponse<User>> =
        client.get(
            path = "users",
            query = buildMap {
                query?.let { put("q", it) }
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                gender?.let { put("gender", it) }
                location?.let { put("location", it) }
                maxAge?.let { put("maxAge", it) }
                minAge?.let { put("minAge", it) }
            }
        )
}
