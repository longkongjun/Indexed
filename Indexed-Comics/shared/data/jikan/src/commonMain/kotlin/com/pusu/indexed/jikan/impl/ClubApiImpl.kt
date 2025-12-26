package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.ClubApi
import com.pusu.indexed.jikan.models.club.*
import com.pusu.indexed.jikan.models.common.*

/**
 * ClubApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class ClubApiImpl(
    private val client: JikanClient
) : ClubApi {
    
    override suspend fun getClubById(id: Int): Result<JikanResponse<Club>> =
        client.get(path = "clubs/$id")
    
    override suspend fun getClubMembers(
        id: Int,
        page: Int?
    ): Result<JikanPageResponse<ClubMember>> =
        client.get(
            path = "clubs/$id/members",
            query = buildMap {
                page?.let { put("page", it) }
            }
        )
    
    override suspend fun getClubStaff(id: Int): Result<JikanResponse<List<ClubStaff>>> =
        client.get(path = "clubs/$id/staff")
    
    override suspend fun getClubRelations(id: Int): Result<JikanResponse<ClubRelations>> =
        client.get(path = "clubs/$id/relations")
    
    override suspend fun searchClubs(
        query: String?,
        page: Int?,
        limit: Int?,
        type: String?,
        category: String?,
        orderBy: String?,
        sort: String?
    ): Result<JikanPageResponse<Club>> =
        client.get(
            path = "clubs",
            query = buildMap {
                query?.let { put("q", it) }
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                type?.let { put("type", it) }
                category?.let { put("category", it) }
                orderBy?.let { put("order_by", it) }
                sort?.let { put("sort", it) }
            }
        )
}
