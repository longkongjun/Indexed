package com.pusu.indexed.jikan.models.season

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 季度信息
 * 
 * @property year 年份
 * @property seasons 季度列表
 */
@Serializable
data class Season(
    @SerialName("year")
    val year: Int? = null,
    
    @SerialName("seasons")
    val seasons: List<String>? = null
)

