package com.pusu.indexed.jikan.models.manga

import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.anime.CharacterMeta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 漫画数据模型
 * 
 * @property malId MyAnimeList ID
 * @property url MAL 页面 URL
 * @property images 图片集合
 * @property approved 是否已审核
 * @property titles 标题列表
 * @property title 主标题
 * @property titleEnglish 英文标题
 * @property titleJapanese 日文标题
 * @property titleSynonyms 其他标题
 * @property type 漫画类型（Manga, Novel, Manhwa, Manhua等）
 * @property chapters 总章数
 * @property volumes 总卷数
 * @property status 连载状态
 * @property publishing 是否正在连载
 * @property published 出版日期
 * @property score 评分
 * @property scoredBy 评分人数
 * @property rank 排名
 * @property popularity 人气排名
 * @property members 成员数
 * @property favorites 收藏数
 * @property synopsis 简介
 * @property background 背景信息
 * @property authors 作者列表
 * @property serializations 连载杂志列表
 * @property genres 类型标签
 * @property explicitGenres 显式类型标签
 * @property themes 主题标签
 * @property demographics 受众群体标签
 */
@Serializable
data class Manga(
    // 必需字段
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: Images,
    
    @SerialName("title")
    val title: String,
    
    @SerialName("type")
    val type: String,
    
    // 可选字段
    @SerialName("approved")
    val approved: Boolean? = null,
    
    @SerialName("titles")
    val titles: List<Title>? = null,
    
    @SerialName("title_english")
    val titleEnglish: String? = null,
    
    @SerialName("title_japanese")
    val titleJapanese: String? = null,
    
    @SerialName("title_synonyms")
    val titleSynonyms: List<String>? = null,
    
    @SerialName("chapters")
    val chapters: Int? = null,
    
    @SerialName("volumes")
    val volumes: Int? = null,
    
    @SerialName("status")
    val status: String? = null,
    
    @SerialName("publishing")
    val publishing: Boolean? = null,
    
    @SerialName("published")
    val published: DateRange? = null,
    
    @SerialName("score")
    val score: Double? = null,
    
    @SerialName("scored_by")
    val scoredBy: Int? = null,
    
    @SerialName("rank")
    val rank: Int? = null,
    
    @SerialName("popularity")
    val popularity: Int? = null,
    
    @SerialName("members")
    val members: Int? = null,
    
    @SerialName("favorites")
    val favorites: Int? = null,
    
    @SerialName("synopsis")
    val synopsis: String? = null,
    
    @SerialName("background")
    val background: String? = null,
    
    @SerialName("authors")
    val authors: List<MalUrl>? = null,
    
    @SerialName("serializations")
    val serializations: List<MalUrl>? = null,
    
    @SerialName("genres")
    val genres: List<MalUrl>? = null,
    
    @SerialName("explicit_genres")
    val explicitGenres: List<MalUrl>? = null,
    
    @SerialName("themes")
    val themes: List<MalUrl>? = null,
    
    @SerialName("demographics")
    val demographics: List<MalUrl>? = null,
    
    // 完整信息专属字段
    @SerialName("relations")
    val relations: List<Relation>? = null,
    
    @SerialName("external")
    val external: List<ExternalLink>? = null
)

/**
 * 漫画角色信息
 * 
 * @property character 角色信息
 * @property role 角色类型（Main, Supporting等）
 */
@Serializable
data class MangaCharacter(
    @SerialName("character")
    val character: CharacterMeta? = null,
    
    @SerialName("role")
    val role: String? = null
)

/**
 * 漫画新闻
 * 
 * @property malId MAL ID
 * @property url 新闻 URL
 * @property title 新闻标题
 * @property date 发布日期
 * @property authorUsername 作者用户名
 * @property authorUrl 作者主页 URL
 * @property forumUrl 论坛链接
 * @property images 新闻图片
 * @property comments 评论数
 * @property excerpt 摘要
 */
@Serializable
data class MangaNews(
    @SerialName("mal_id")
    val malId: Int? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("title")
    val title: String? = null,
    
    @SerialName("date")
    val date: String? = null,
    
    @SerialName("author_username")
    val authorUsername: String? = null,
    
    @SerialName("author_url")
    val authorUrl: String? = null,
    
    @SerialName("forum_url")
    val forumUrl: String? = null,
    
    @SerialName("images")
    val images: Images? = null,
    
    @SerialName("comments")
    val comments: Int? = null,
    
    @SerialName("excerpt")
    val excerpt: String? = null
)

/**
 * 漫画图片信息
 * 
 * @property jpg JPG 格式图片
 * @property webp WEBP 格式图片
 */
@Serializable
data class MangaPicture(
    @SerialName("jpg")
    val jpg: PictureFormat? = null,
    
    @SerialName("webp")
    val webp: PictureFormat? = null
)

/**
 * 图片格式信息
 * 
 * @property imageUrl 图片 URL
 * @property smallImageUrl 小图 URL
 * @property largeImageUrl 大图 URL
 */
@Serializable
data class PictureFormat(
    @SerialName("image_url")
    val imageUrl: String? = null,
    
    @SerialName("small_image_url")
    val smallImageUrl: String? = null,
    
    @SerialName("large_image_url")
    val largeImageUrl: String? = null
)

/**
 * 漫画统计信息
 * 
 * @property reading 阅读中人数
 * @property completed 已完成人数
 * @property onHold 搁置人数
 * @property dropped 弃坑人数
 * @property planToRead 计划阅读人数
 * @property total 总人数
 * @property scores 评分分布
 */
@Serializable
data class MangaStatistics(
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
    
    @SerialName("total")
    val total: Int? = null,
    
    @SerialName("scores")
    val scores: List<ScoreStats>? = null
)

/**
 * 漫画评论
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
 * @property chaptersRead 已读章数
 * @property user 用户信息
 */
@Serializable
data class MangaReview(
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
    
    @SerialName("chapters_read")
    val chaptersRead: Int? = null,
    
    @SerialName("user")
    val user: UserMeta? = null
)

/**
 * 评论反应统计
 * 
 * @property overall 总体评价
 * @property nice 好评数
 * @property loveIt 喜爱数
 * @property funny 有趣数
 * @property confusing 困惑数
 * @property informative 信息丰富数
 * @property wellWritten 写得好数
 * @property creative 创意数
 */
@Serializable
data class ReviewReactions(
    @SerialName("overall")
    val overall: Int? = null,
    
    @SerialName("nice")
    val nice: Int? = null,
    
    @SerialName("love_it")
    val loveIt: Int? = null,
    
    @SerialName("funny")
    val funny: Int? = null,
    
    @SerialName("confusing")
    val confusing: Int? = null,
    
    @SerialName("informative")
    val informative: Int? = null,
    
    @SerialName("well_written")
    val wellWritten: Int? = null,
    
    @SerialName("creative")
    val creative: Int? = null
)

