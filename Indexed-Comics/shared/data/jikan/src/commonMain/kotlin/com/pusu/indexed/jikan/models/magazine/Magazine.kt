package com.pusu.indexed.jikan.models.magazine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 杂志/出版社数据模型
 * 
 * @property malId MAL ID
 * @property name 杂志名称
 * @property url MAL URL
 * @property count 关联作品数
 */
@Serializable
data class Magazine(
    // 必需字段
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("url")
    val url: String,
    
    // 可选字段
    
    @SerialName("count")
    val count: Int? = null
)

