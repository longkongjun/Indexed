package com.pusu.indexed.jikan.models.anime

import com.pusu.indexed.jikan.models.common.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 动漫数据模型
 * 
 * @property malId MyAnimeList ID
 * @property url MAL 页面 URL
 * @property images 图片集合
 * @property trailer 预告片信息
 * @property approved 是否已审核
 * @property titles 标题列表
 * @property title 主标题
 * @property titleEnglish 英文标题
 * @property titleJapanese 日文标题
 * @property titleSynonyms 其他标题
 * @property type 动漫类型（TV, Movie, OVA等）
 * @property source 原作来源
 * @property episodes 总集数
 * @property status 播放状态
 * @property airing 是否正在播放
 * @property aired 播放日期
 * @property duration 单集时长
 * @property rating 年龄分级
 * @property score 评分
 * @property scoredBy 评分人数
 * @property rank 排名
 * @property popularity 人气排名
 * @property members 成员数
 * @property favorites 收藏数
 * @property synopsis 简介
 * @property background 背景信息
 * @property season 播放季度
 * @property year 播放年份
 * @property broadcast 播放时间
 * @property producers 制作公司列表
 * @property licensors 授权公司列表
 * @property studios 动画工作室列表
 * @property genres 类型标签
 * @property explicitGenres 显式类型标签
 * @property themes 主题标签
 * @property demographics 受众群体标签
 */
@Serializable
data class Anime(
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
    @SerialName("trailer")
    val trailer: Trailer? = null,
    
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
    
    @SerialName("source")
    val source: String? = null,
    
    @SerialName("episodes")
    val episodes: Int? = null,
    
    @SerialName("status")
    val status: String? = null,
    
    @SerialName("airing")
    val airing: Boolean? = null,
    
    @SerialName("aired")
    val aired: DateRange? = null,
    
    @SerialName("duration")
    val duration: String? = null,
    
    @SerialName("rating")
    val rating: String? = null,
    
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
    
    @SerialName("season")
    val season: String? = null,
    
    @SerialName("year")
    val year: Int? = null,
    
    @SerialName("broadcast")
    val broadcast: Broadcast? = null,
    
    @SerialName("producers")
    val producers: List<MalUrl>? = null,
    
    @SerialName("licensors")
    val licensors: List<MalUrl>? = null,
    
    @SerialName("studios")
    val studios: List<MalUrl>? = null,
    
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
    
    @SerialName("theme")
    val theme: AnimeTheme? = null,
    
    @SerialName("external")
    val external: List<ExternalLink>? = null,
    
    @SerialName("streaming")
    val streaming: List<ExternalLink>? = null
)

/**
 * 预告片信息
 * 
 * @property youtubeId YouTube 视频 ID
 * @property url YouTube 视频 URL
 * @property embedUrl 嵌入式播放 URL
 * @property images 预览图片
 */
@Serializable
data class Trailer(
    @SerialName("youtube_id")
    val youtubeId: String? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("embed_url")
    val embedUrl: String? = null,
    
    @SerialName("images")
    val images: TrailerImages? = null
)

/**
 * 预告片图片
 * 
 * @property imageUrl 图片 URL
 * @property smallImageUrl 小图 URL
 * @property mediumImageUrl 中图 URL
 * @property largeImageUrl 大图 URL
 * @property maximumImageUrl 最大图 URL
 */
@Serializable
data class TrailerImages(
    @SerialName("image_url")
    val imageUrl: String? = null,
    
    @SerialName("small_image_url")
    val smallImageUrl: String? = null,
    
    @SerialName("medium_image_url")
    val mediumImageUrl: String? = null,
    
    @SerialName("large_image_url")
    val largeImageUrl: String? = null,
    
    @SerialName("maximum_image_url")
    val maximumImageUrl: String? = null
)

/**
 * 播放时间信息
 * 
 * @property day 星期几
 * @property time 播放时间
 * @property timezone 时区
 * @property string 时间字符串
 */
@Serializable
data class Broadcast(
    @SerialName("day")
    val day: String? = null,
    
    @SerialName("time")
    val time: String? = null,
    
    @SerialName("timezone")
    val timezone: String? = null,
    
    @SerialName("string")
    val string: String? = null
)

/**
 * 动漫主题曲信息
 * 
 * @property openings 片头曲列表
 * @property endings 片尾曲列表
 */
@Serializable
data class AnimeTheme(
    @SerialName("openings")
    val openings: List<String>? = null,
    
    @SerialName("endings")
    val endings: List<String>? = null
)

/**
 * 动漫角色信息
 * 
 * @property character 角色信息
 * @property role 角色类型（Main, Supporting等）
 * @property favorites 收藏数
 * @property voiceActors 声优列表
 */
@Serializable
data class AnimeCharacter(
    @SerialName("character")
    val character: CharacterMeta? = null,
    
    @SerialName("role")
    val role: String? = null,
    
    @SerialName("favorites")
    val favorites: Int? = null,
    
    @SerialName("voice_actors")
    val voiceActors: List<VoiceActor>? = null
)

/**
 * 角色元信息
 * 
 * @property malId MAL ID（必需）
 * @property url MAL URL（必需）
 * @property images 角色图片（必需）
 * @property name 角色名称（必需）
 */
@Serializable
data class CharacterMeta(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: Images,
    
    @SerialName("name")
    val name: String
)

/**
 * 声优信息
 * 
 * @property person 人物信息
 * @property language 配音语言
 */
@Serializable
data class VoiceActor(
    @SerialName("person")
    val person: PersonMeta? = null,
    
    @SerialName("language")
    val language: String? = null
)

/**
 * 人物元信息
 * 
 * @property malId MAL ID（必需）
 * @property url MAL URL（必需）
 * @property images 人物图片（必需）
 * @property name 人物名称（必需）
 */
@Serializable
data class PersonMeta(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: Images,
    
    @SerialName("name")
    val name: String
)

/**
 * 动漫工作人员
 * 
 * @property person 人物信息
 * @property positions 职位列表
 */
@Serializable
data class AnimeStaff(
    @SerialName("person")
    val person: PersonMeta? = null,
    
    @SerialName("positions")
    val positions: List<String>? = null
)

/**
 * 动漫剧集信息
 * 
 * @property malId MAL ID
 * @property url MAL URL
 * @property title 标题
 * @property titleJapanese 日文标题
 * @property titleRomanji 罗马音标题
 * @property duration 时长（秒）
 * @property aired 播放日期
 * @property filler 是否填充集
 * @property recap 是否回顾集
 * @property forumUrl 论坛链接
 */
@Serializable
data class AnimeEpisode(
    @SerialName("mal_id")
    val malId: Int? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("title")
    val title: String? = null,
    
    @SerialName("title_japanese")
    val titleJapanese: String? = null,
    
    @SerialName("title_romanji")
    val titleRomanji: String? = null,
    
    @SerialName("duration")
    val duration: Int? = null,
    
    @SerialName("aired")
    val aired: String? = null,
    
    @SerialName("filler")
    val filler: Boolean? = null,
    
    @SerialName("recap")
    val recap: Boolean? = null,
    
    @SerialName("forum_url")
    val forumUrl: String? = null
)

/**
 * 动漫新闻
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
data class AnimeNews(
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
 * 论坛话题
 * 
 * @property malId MAL ID
 * @property url 话题 URL
 * @property title 话题标题
 * @property date 发布日期
 * @property authorUsername 作者用户名
 * @property authorUrl 作者主页 URL
 * @property comments 评论数
 * @property lastComment 最后评论信息
 */
@Serializable
data class ForumTopic(
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
    
    @SerialName("comments")
    val comments: Int? = null,
    
    @SerialName("last_comment")
    val lastComment: LastComment? = null
)

/**
 * 最后评论信息
 * 
 * @property url 评论 URL
 * @property authorUsername 评论者用户名
 * @property authorUrl 评论者主页 URL
 * @property date 评论日期
 */
@Serializable
data class LastComment(
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("author_username")
    val authorUsername: String? = null,
    
    @SerialName("author_url")
    val authorUrl: String? = null,
    
    @SerialName("date")
    val date: String? = null
)

/**
 * 动漫视频集合
 * 
 * @property promos 宣传视频列表
 * @property episodes 剧集视频列表
 * @property musicVideos 音乐视频列表
 */
@Serializable
data class AnimeVideos(
    @SerialName("promos")
    val promos: List<PromoVideo>? = null,
    
    @SerialName("episodes")
    val episodes: List<EpisodeVideo>? = null,
    
    @SerialName("music_videos")
    val musicVideos: List<MusicVideo>? = null
)

/**
 * 宣传视频
 * 
 * @property title 标题
 * @property trailer 预告片信息
 */
@Serializable
data class PromoVideo(
    @SerialName("title")
    val title: String? = null,
    
    @SerialName("trailer")
    val trailer: Trailer? = null
)

/**
 * 剧集视频
 * 
 * @property malId MAL ID
 * @property url 视频 URL
 * @property title 标题
 * @property episode 集数
 * @property images 视频图片
 */
@Serializable
data class EpisodeVideo(
    @SerialName("mal_id")
    val malId: Int? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("title")
    val title: String? = null,
    
    @SerialName("episode")
    val episode: String? = null,
    
    @SerialName("images")
    val images: Images? = null
)

/**
 * 音乐视频
 * 
 * @property title 标题
 * @property video 视频信息
 * @property meta 元信息
 */
@Serializable
data class MusicVideo(
    @SerialName("title")
    val title: String? = null,
    
    @SerialName("video")
    val video: Trailer? = null,
    
    @SerialName("meta")
    val meta: MusicVideoMeta? = null
)

/**
 * 音乐视频元信息
 * 
 * @property title 标题
 * @property author 作者
 */
@Serializable
data class MusicVideoMeta(
    @SerialName("title")
    val title: String? = null,
    
    @SerialName("author")
    val author: String? = null
)

/**
 * 图片信息
 * 
 * @property jpg JPG 格式图片
 * @property webp WEBP 格式图片
 */
@Serializable
data class Picture(
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
 * 动漫统计信息
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
data class AnimeStatistics(
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
 * 动漫评论
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
 * @property episodesWatched 已看集数
 * @property user 用户信息
 */
@Serializable
data class AnimeReview(
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

