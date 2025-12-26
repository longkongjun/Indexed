package com.pusu.indexed.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

actual class HttpClientFactory {
    actual fun create(): HttpClient = HttpClient(Js) {
        install(ContentNegotiation) { json(NetworkJson.json) }
        install(Logging) { level = LogLevel.INFO }
    }
}
