package com.pusu.indexed.jikan.models.recommendation

import com.pusu.indexed.jikan.models.character.AnimeMeta
import com.pusu.indexed.jikan.models.character.MangaMeta
import com.pusu.indexed.jikan.models.common.UserMeta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 全局推荐数据模型（最新推荐）
 * 
 * @property malId MAL ID
 * @property entry 推荐条目列表
 * @property content 推荐内容描述
 * @property user 推荐用户
 * @property date 推荐日期
 */
@Serializable
data class RecentRecommendation(
    @SerialName("mal_id")
    val malId: String? = null,
    
    @SerialName("entry")
    val entry: List<RecommendationEntry>? = null,
    
    @SerialName("content")
    val content: String? = null,
    
    @SerialName("user")
    val user: UserMeta? = null,
    
    @SerialName("date")
    val date: String? = null
)

/**
 * 推荐条目
 * 
 * @property malId MAL ID
 * @property url MAL URL
 * @property images 图片
 * @property title 标题
 */
@Serializable
data class RecommendationEntry(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: com.pusu.indexed.jikan.models.common.Images,
    
    @SerialName("title")
    val title: String
)

