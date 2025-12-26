package com.pusu.indexed.jikan

import com.pusu.indexed.core.network.NetworkClient
import com.pusu.indexed.core.network.getJson
import com.pusu.indexed.jikan.models.anime.Anime
import com.pusu.indexed.jikan.models.common.JikanPageResponse
import io.ktor.client.HttpClient

class JikanClient(
    val baseUrl: String = "https://api.jikan.moe/v4",
    val httpClient: HttpClient = NetworkClient.httpClient,
) {
    suspend inline fun <reified T> get(
        path: String,
        query: Map<String, Any?> = emptyMap(),
    ): Result<T> {
        val normalizedBase = if (baseUrl.endsWith("/")) baseUrl.dropLast(1) else baseUrl
        val url = "$normalizedBase/$path"
        return httpClient.getJson(url, query)
    }
}
