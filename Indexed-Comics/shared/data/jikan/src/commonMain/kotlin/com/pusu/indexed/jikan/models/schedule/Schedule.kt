package com.pusu.indexed.jikan.models.schedule

import com.pusu.indexed.jikan.models.anime.Anime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 播放时间表数据模型
 * 
 * @property monday 周一播放的动漫列表
 * @property tuesday 周二播放的动漫列表
 * @property wednesday 周三播放的动漫列表
 * @property thursday 周四播放的动漫列表
 * @property friday 周五播放的动漫列表
 * @property saturday 周六播放的动漫列表
 * @property sunday 周日播放的动漫列表
 * @property other 其他时间播放的动漫列表
 * @property unknown 未知播放时间的动漫列表
 */
@Serializable
data class Schedule(
    @SerialName("monday")
    val monday: List<Anime>? = null,
    
    @SerialName("tuesday")
    val tuesday: List<Anime>? = null,
    
    @SerialName("wednesday")
    val wednesday: List<Anime>? = null,
    
    @SerialName("thursday")
    val thursday: List<Anime>? = null,
    
    @SerialName("friday")
    val friday: List<Anime>? = null,
    
    @SerialName("saturday")
    val saturday: List<Anime>? = null,
    
    @SerialName("sunday")
    val sunday: List<Anime>? = null,
    
    @SerialName("other")
    val other: List<Anime>? = null,
    
    @SerialName("unknown")
    val unknown: List<Anime>? = null
)

