package com.pusu.indexed.jikan.models.watch

import com.pusu.indexed.jikan.models.anime.PromoVideo
import com.pusu.indexed.jikan.models.anime.EpisodeVideo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 最近宣传视频数据模型
 * 
 * @property entry 作品信息
 * @property trailers 预告片列表
 */
@Serializable
data class RecentPromoVideo(
    @SerialName("entry")
    val entry: WatchEntry? = null,
    
    @SerialName("trailers")
    val trailers: List<PromoVideo>? = null
)

/**
 * 最近剧集视频数据模型
 * 
 * @property entry 作品信息
 * @property episodes 剧集列表
 * @property regionLocked 是否区域锁定
 */
@Serializable
data class RecentEpisodeVideo(
    @SerialName("entry")
    val entry: WatchEntry? = null,
    
    @SerialName("episodes")
    val episodes: List<EpisodeVideo>? = null,
    
    @SerialName("region_locked")
    val regionLocked: Boolean? = null
)

/**
 * 观看条目信息
 * 
 * @property malId MAL ID
 * @property url MAL URL
 * @property title 标题
 */
@Serializable
data class WatchEntry(
    @SerialName("mal_id")
    val malId: Int? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("title")
    val title: String? = null
)

