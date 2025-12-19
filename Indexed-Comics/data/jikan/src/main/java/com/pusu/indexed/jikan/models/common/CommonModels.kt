package com.pusu.indexed.jikan.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 图片信息
 * 
 * @property jpg JPG 格式图片（必需）
 * @property webp WEBP 格式图片（可选）
 */
@Serializable
data class Images(
    @SerialName("jpg")
    val jpg: ImageFormat,
    
    @SerialName("webp")
    val webp: ImageFormat? = null
)

/**
 * 图片格式信息
 * 
 * @property imageUrl 普通尺寸图片 URL（必需）
 * @property smallImageUrl 小尺寸图片 URL（可选）
 * @property largeImageUrl 大尺寸图片 URL（可选）
 */
@Serializable
data class ImageFormat(
    @SerialName("image_url")
    val imageUrl: String,
    
    @SerialName("small_image_url")
    val smallImageUrl: String? = null,
    
    @SerialName("large_image_url")
    val largeImageUrl: String? = null
)

/**
 * 标题信息
 * 
 * @property type 标题类型（可选）
 * @property title 标题文本（必需）
 */
@Serializable
data class Title(
    @SerialName("type")
    val type: String? = null,
    
    @SerialName("title")
    val title: String
)

/**
 * 日期范围
 * 
 * @property from 开始日期
 * @property to 结束日期
 * @property prop 日期属性
 * @property string 日期字符串表示
 */
@Serializable
data class DateRange(
    @SerialName("from")
    val from: String? = null,
    
    @SerialName("to")
    val to: String? = null,
    
    @SerialName("prop")
    val prop: DateProp? = null,
    
    @SerialName("string")
    val string: String? = null
)

/**
 * 日期属性
 * 
 * @property from 开始日期详情
 * @property to 结束日期详情
 */
@Serializable
data class DateProp(
    @SerialName("from")
    val from: DateDetail? = null,
    
    @SerialName("to")
    val to: DateDetail? = null
)

/**
 * 日期详情
 * 
 * @property day 日
 * @property month 月
 * @property year 年
 */
@Serializable
data class DateDetail(
    @SerialName("day")
    val day: Int? = null,
    
    @SerialName("month")
    val month: Int? = null,
    
    @SerialName("year")
    val year: Int? = null
)

/**
 * 分类信息（类型、主题等）
 * 
 * @property malId MAL ID（必需）
 * @property type 分类类型（可选）
 * @property name 分类名称（必需）
 * @property url MAL URL（必需）
 */
@Serializable
data class MalUrl(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("type")
    val type: String? = null,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("url")
    val url: String
)

/**
 * 外部链接
 * 
 * @property name 链接名称（必需）
 * @property url 链接地址（必需）
 */
@Serializable
data class ExternalLink(
    @SerialName("name")
    val name: String,
    
    @SerialName("url")
    val url: String
)

/**
 * 关系条目
 * 
 * @property relation 关系类型
 * @property entry 关系条目列表
 */
@Serializable
data class Relation(
    @SerialName("relation")
    val relation: String? = null,
    
    @SerialName("entry")
    val entry: List<RelationEntry>? = null
)

/**
 * 关系条目详情
 * 
 * @property malId MAL ID（必需）
 * @property type 类型（可选）
 * @property name 名称（必需）
 * @property url MAL URL（必需）
 */
@Serializable
data class RelationEntry(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("type")
    val type: String? = null,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("url")
    val url: String
)

/**
 * 推荐条目
 * 
 * @property entry 推荐的作品
 * @property url 推荐链接
 * @property votes 投票数
 */
@Serializable
data class Recommendation(
    @SerialName("entry")
    val entry: RelationEntry? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("votes")
    val votes: Int? = null
)

/**
 * 用户更新信息
 * 
 * @property user 用户信息
 * @property score 评分
 * @property status 状态
 * @property episodesSeen 已看集数（动漫）
 * @property chaptersRead 已读章数（漫画）
 * @property volumesRead 已读卷数（漫画）
 * @property date 更新日期
 */
@Serializable
data class UserUpdate(
    @SerialName("user")
    val user: UserMeta? = null,
    
    @SerialName("score")
    val score: Double? = null,
    
    @SerialName("status")
    val status: String? = null,
    
    @SerialName("episodes_seen")
    val episodesSeen: Int? = null,
    
    @SerialName("chapters_read")
    val chaptersRead: Int? = null,
    
    @SerialName("volumes_read")
    val volumesRead: Int? = null,
    
    @SerialName("date")
    val date: String? = null
)

/**
 * 用户元信息
 * 
 * @property username 用户名（必需）
 * @property url 用户主页 URL（必需）
 * @property images 用户头像（必需）
 */
@Serializable
data class UserMeta(
    @SerialName("username")
    val username: String,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: Images
)

/**
 * 统计信息
 * 
 * @property watching 观看中人数
 * @property completed 已完成人数
 * @property onHold 搁置人数
 * @property dropped 弃番人数
 * @property planToWatch 计划观看人数
 * @property total 总人数
 * @property scores 评分分布
 */
@Serializable
data class Statistics(
    @SerialName("watching")
    val watching: Int? = null,
    
    @SerialName("completed")
    val completed: Int? = null,
    
    @SerialName("on_hold")
    val onHold: Int? = null,
    
    @SerialName("dropped")
    val dropped: Int? = null,
    
    @SerialName("plan_to_watch")
    val planToWatch: Int? = null,
    
    @SerialName("total")
    val total: Int? = null,
    
    @SerialName("scores")
    val scores: List<ScoreStats>? = null
)

/**
 * 评分统计
 * 
 * @property score 分数
 * @property votes 投票数
 * @property percentage 百分比
 */
@Serializable
data class ScoreStats(
    @SerialName("score")
    val score: Int? = null,
    
    @SerialName("votes")
    val votes: Int? = null,
    
    @SerialName("percentage")
    val percentage: Double? = null
)

