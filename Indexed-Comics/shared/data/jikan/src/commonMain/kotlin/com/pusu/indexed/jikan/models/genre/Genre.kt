package com.pusu.indexed.jikan.models.genre

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 类型/题材数据模型
 * 
 * @property malId MAL ID
 * @property name 类型名称
 * @property url MAL URL
 * @property count 相关作品数
 */
@Serializable
data class Genre(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("count")
    val count: Int? = null
)

