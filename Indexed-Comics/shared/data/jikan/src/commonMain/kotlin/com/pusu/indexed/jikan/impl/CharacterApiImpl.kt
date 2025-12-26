package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.CharacterApi
import com.pusu.indexed.jikan.models.character.*
import com.pusu.indexed.jikan.models.common.*

/**
 * CharacterApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class CharacterApiImpl(
    private val client: JikanClient
) : CharacterApi {
    
    override suspend fun getCharacterById(id: Int): Result<JikanResponse<Character>> =
        client.get(path = "characters/$id")
    
    override suspend fun getCharacterFullById(id: Int): Result<JikanResponse<Character>> =
        client.get(path = "characters/$id/full")
    
    override suspend fun getCharacterPictures(id: Int): Result<JikanResponse<List<CharacterPicture>>> =
        client.get(path = "characters/$id/pictures")
    
    override suspend fun searchCharacters(
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sort: String?
    ): Result<JikanPageResponse<Character>> =
        client.get(
            path = "characters",
            query = buildMap {
                query?.let { put("q", it) }
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
                orderBy?.let { put("order_by", it) }
                sort?.let { put("sort", it) }
            }
        )
    
    override suspend fun getTopCharacters(
        page: Int?,
        limit: Int?
    ): Result<JikanPageResponse<Character>> =
        client.get(
            path = "top/characters",
            query = buildMap {
                page?.let { put("page", it) }
                limit?.let { put("limit", it) }
            }
        )
}
