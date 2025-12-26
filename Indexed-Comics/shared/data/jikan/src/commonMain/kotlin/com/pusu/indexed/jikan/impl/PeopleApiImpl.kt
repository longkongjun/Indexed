package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.PeopleApi
import com.pusu.indexed.jikan.models.people.*
import com.pusu.indexed.jikan.models.common.*

/**
 * PeopleApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class PeopleApiImpl(
    private val client: JikanClient
) : PeopleApi {
    
    override suspend fun getPersonById(id: Int): Result<JikanResponse<Person>> =
        client.get(path = "people/$id")
    
    override suspend fun getPersonFullById(id: Int): Result<JikanResponse<Person>> =
        client.get(path = "people/$id/full")
    
    override suspend fun getPersonPictures(id: Int): Result<JikanResponse<List<PersonPicture>>> =
        client.get(path = "people/$id/pictures")
    
    override suspend fun searchPeople(
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sort: String?
    ): Result<JikanPageResponse<Person>> =
        client.get(
            path = "people",
            query = buildMap {
                query?.let { put("q", it) }
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                orderBy?.let { put("order_by", it) }
                sort?.let { put("sort", it) }
            }
        )
    
    override suspend fun getTopPeople(
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<Person>> =
        client.get(
            path = "top/people",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
}
