package com.pusu.indexed.jikan.models.producer

import com.pusu.indexed.jikan.models.common.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 制作公司数据模型
 * 
 * @property malId MAL ID
 * @property url MAL URL
 * @property titles 标题列表
 * @property images 图片
 * @property favorites 收藏数
 * @property count 关联作品数
 * @property established 成立日期
 * @property about 公司介绍
 */
@Serializable
data class Producer(
    // 必需字段
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    // 可选字段
    
    @SerialName("titles")
    val titles: List<Title>? = null,
    
    @SerialName("images")
    val images: Images? = null,
    
    @SerialName("favorites")
    val favorites: Int? = null,
    
    @SerialName("count")
    val count: Int? = null,
    
    @SerialName("established")
    val established: String? = null,
    
    @SerialName("about")
    val about: String? = null,
    
    @SerialName("external")
    val external: List<ExternalLink>? = null
)

