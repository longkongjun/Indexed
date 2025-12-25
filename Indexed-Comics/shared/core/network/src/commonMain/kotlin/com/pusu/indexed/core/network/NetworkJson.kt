package com.pusu.indexed.core.network

import kotlinx.serialization.json.Json

object NetworkJson {
    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
}
