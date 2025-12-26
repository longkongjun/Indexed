package com.pusu.indexed.jikan.models.review

import com.pusu.indexed.jikan.models.common.UserMeta
import com.pusu.indexed.jikan.models.manga.ReviewReactions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 全局评论数据模型（最新评论）
 * 
 * @property malId MAL ID
 * @property url 评论 URL
 * @property type 类型
 * @property reactions 反应统计
 * @property date 发布日期
 * @property review 评论内容
 * @property score 评分
 * @property tags 标签
 * @property isSpoiler 是否剧透
 * @property isPreliminary 是否初步评论
 * @property episodesWatched 已看集数（动漫）
 * @property chaptersRead 已读章数（漫画）
 * @property user 用户信息
 * @property entry 作品信息
 */
@Serializable
data class RecentReview(
    @SerialName("mal_id")
    val malId: Int? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("type")
    val type: String? = null,
    
    @SerialName("reactions")
    val reactions: ReviewReactions? = null,
    
    @SerialName("date")
    val date: String? = null,
    
    @SerialName("review")
    val review: String? = null,
    
    @SerialName("score")
    val score: Int? = null,
    
    @SerialName("tags")
    val tags: List<String>? = null,
    
    @SerialName("is_spoiler")
    val isSpoiler: Boolean? = null,
    
    @SerialName("is_preliminary")
    val isPreliminary: Boolean? = null,
    
    @SerialName("episodes_watched")
    val episodesWatched: Int? = null,
    
    @SerialName("chapters_read")
    val chaptersRead: Int? = null,
    
    @SerialName("user")
    val user: UserMeta? = null,
    
    @SerialName("entry")
    val entry: ReviewEntry? = null
)

/**
 * 评论作品条目
 * 
 * @property malId MAL ID
 * @property url MAL URL
 * @property images 图片
 * @property title 标题
 */
@Serializable
data class ReviewEntry(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: com.pusu.indexed.jikan.models.common.Images,
    
    @SerialName("title")
    val title: String
)

