package com.pusu.indexed.jikan.network

import kotlinx.serialization.json.Json

/**
 * 全项目统一的 Kotlin Serialization Json 配置。
 *
 * - ignoreUnknownKeys: 兼容服务端新增字段
 * - isLenient/coerceInputValues: 提升容错性
 */
object JikanJson {
    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
}


