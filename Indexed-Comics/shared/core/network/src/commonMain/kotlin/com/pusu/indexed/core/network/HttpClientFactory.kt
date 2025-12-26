package com.pusu.indexed.core.network

import io.ktor.client.HttpClient

expect class HttpClientFactory() {
    fun create(): HttpClient
}
