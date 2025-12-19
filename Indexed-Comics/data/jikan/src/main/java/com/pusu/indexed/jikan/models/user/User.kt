package com.pusu.indexed.jikan.models.user

import com.pusu.indexed.jikan.models.common.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 用户数据模型
 * 
 * @property malId MAL ID
 * @property username 用户名
 * @property url MAL URL
 * @property images 用户头像
 * @property lastOnline 最后在线时间
 * @property gender 性别
 * @property birthday 生日
 * @property location 所在地
 * @property joined 加入日期
 */
@Serializable
data class User(
    // 必需字段
    @SerialName("username")
    val username: String,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: Images,
    
    // 可选字段（malId 可能不返回）
    @SerialName("mal_id")
    val malId: Int? = null,
    
    @SerialName("last_online")
    val lastOnline: String? = null,
    
    @SerialName("gender")
    val gender: String? = null,
    
    @SerialName("birthday")
    val birthday: String? = null,
    
    @SerialName("location")
    val location: String? = null,
    
    @SerialName("joined")
    val joined: String? = null,
    
    // 完整信息专属字段
    @SerialName("statistics")
    val statistics: UserStatistics? = null,
    
    @SerialName("favorites")
    val favorites: UserFavorites? = null,
    
    @SerialName("about")
    val about: String? = null,
    
    @SerialName("external")
    val external: List<ExternalLink>? = null
)

/**
 * 用户统计信息
 * 
 * @property anime 动漫统计
 * @property manga 漫画统计
 */
@Serializable
data class UserStatistics(
    @SerialName("anime")
    val anime: UserAnimeStatistics? = null,
    
    @SerialName("manga")
    val manga: UserMangaStatistics? = null
)

/**
 * 用户动漫统计
 * 
 * @property daysWatched 观看天数
 * @property meanScore 平均评分
 * @property watching 观看中数量
 * @property completed 已完成数量
 * @property onHold 搁置数量
 * @property dropped 弃番数量
 * @property planToWatch 计划观看数量
 * @property totalEntries 总条目数
 * @property rewatched 重看次数
 * @property episodesWatched 观看集数
 */
@Serializable
data class UserAnimeStatistics(
    @SerialName("days_watched")
    val daysWatched: Double? = null,
    
    @SerialName("mean_score")
    val meanScore: Double? = null,
    
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
    
    @SerialName("total_entries")
    val totalEntries: Int? = null,
    
    @SerialName("rewatched")
    val rewatched: Int? = null,
    
    @SerialName("episodes_watched")
    val episodesWatched: Int? = null
)

/**
 * 用户漫画统计
 * 
 * @property daysRead 阅读天数
 * @property meanScore 平均评分
 * @property reading 阅读中数量
 * @property completed 已完成数量
 * @property onHold 搁置数量
 * @property dropped 弃坑数量
 * @property planToRead 计划阅读数量
 * @property totalEntries 总条目数
 * @property reread 重读次数
 * @property chaptersRead 阅读章数
 * @property volumesRead 阅读卷数
 */
@Serializable
data class UserMangaStatistics(
    @SerialName("days_read")
    val daysRead: Double? = null,
    
    @SerialName("mean_score")
    val meanScore: Double? = null,
    
    @SerialName("reading")
    val reading: Int? = null,
    
    @SerialName("completed")
    val completed: Int? = null,
    
    @SerialName("on_hold")
    val onHold: Int? = null,
    
    @SerialName("dropped")
    val dropped: Int? = null,
    
    @SerialName("plan_to_read")
    val planToRead: Int? = null,
    
    @SerialName("total_entries")
    val totalEntries: Int? = null,
    
    @SerialName("reread")
    val reread: Int? = null,
    
    @SerialName("chapters_read")
    val chaptersRead: Int? = null,
    
    @SerialName("volumes_read")
    val volumesRead: Int? = null
)

/**
 * 用户收藏
 * 
 * @property anime 收藏的动漫
 * @property manga 收藏的漫画
 * @property characters 收藏的角色
 * @property people 收藏的人物
 */
@Serializable
data class UserFavorites(
    @SerialName("anime")
    val anime: List<MalUrl>? = null,
    
    @SerialName("manga")
    val manga: List<MalUrl>? = null,
    
    @SerialName("characters")
    val characters: List<MalUrl>? = null,
    
    @SerialName("people")
    val people: List<MalUrl>? = null
)

/**
 * 用户好友
 * 
 * @property user 用户信息
 * @property lastOnline 最后在线时间
 * @property friendsSince 成为好友的时间
 */
@Serializable
data class UserFriend(
    @SerialName("user")
    val user: UserMeta? = null,
    
    @SerialName("last_online")
    val lastOnline: String? = null,
    
    @SerialName("friends_since")
    val friendsSince: String? = null
)

/**
 * 用户历史记录
 * 
 * @property entry 作品信息
 * @property increment 增加数量
 * @property date 日期
 */
@Serializable
data class UserHistory(
    @SerialName("entry")
    val entry: HistoryEntry? = null,
    
    @SerialName("increment")
    val increment: Int? = null,
    
    @SerialName("date")
    val date: String? = null
)

/**
 * 历史记录条目
 * 
 * @property malId MAL ID
 * @property url MAL URL
 * @property title 标题
 * @property images 图片
 */
@Serializable
data class HistoryEntry(
    @SerialName("mal_id")
    val malId: Int? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("title")
    val title: String? = null,
    
    @SerialName("images")
    val images: Images? = null
)

