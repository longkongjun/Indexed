package com.pusu.indexed.jikan.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Jikan API 标准错误响应
 *
 * 对齐 API 文档中描述的 error response:
 * {
 *   "status": 500,
 *   "type": "InternalException",
 *   "message": "Exception Message",
 *   "error": "Exception Trace",
 *   "report_url": "https://github.com..."
 * }
 */
@Serializable
data class JikanErrorResponse(
    /** Returned HTTP Status Code */
    val status: Int? = null,
    /** Thrown Exception */
    val type: String? = null,
    /** Human-readable error message */
    val message: String? = null,
    /** Error response and trace from the API */
    val error: String? = null,
    /** Clicking this would redirect you to a generated GitHub issue */
    @SerialName("report_url")
    val reportUrl: String? = null,
)


