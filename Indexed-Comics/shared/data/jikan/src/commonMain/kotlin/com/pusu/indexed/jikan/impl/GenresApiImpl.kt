package com.pusu.indexed.jikan.impl

import com.pusu.indexed.jikan.JikanClient
import com.pusu.indexed.jikan.api.GenresApi
import com.pusu.indexed.jikan.models.genre.Genre
import com.pusu.indexed.jikan.models.common.*

/**
 * GenresApi 的默认实现。
 * 
 * @property client HTTP 客户端，负责底层网络通信
 */
internal class GenresApiImpl(
    private val client: JikanClient
) : GenresApi {
    
    override suspend fun getAnimeGenres(
        filter: String?
    ): Result<JikanResponse<List<Genre>>> =
        client.get(
            path = "genres/anime",
            query = buildMap {
                filter?.let { put("filter", it) }
            }
        )
    
    override suspend fun getMangaGenres(
        filter: String?
    ): Result<JikanResponse<List<Genre>>> =
        client.get(
            path = "genres/manga",
            query = buildMap {
                filter?.let { put("filter", it) }
            }
        )
}
