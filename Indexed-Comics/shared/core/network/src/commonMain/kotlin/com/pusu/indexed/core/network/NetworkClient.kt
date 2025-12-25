package com.pusu.indexed.core.network

import io.ktor.client.HttpClient

object NetworkClient {
    val httpClient: HttpClient by lazy { HttpClientFactory().create() }
}
