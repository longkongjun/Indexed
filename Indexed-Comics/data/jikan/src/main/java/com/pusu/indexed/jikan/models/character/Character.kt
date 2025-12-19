package com.pusu.indexed.jikan.models.character

import com.pusu.indexed.jikan.models.common.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 角色数据模型
 * 
 * @property malId MyAnimeList ID
 * @property url MAL 页面 URL
 * @property images 图片集合
 * @property name 角色名称
 * @property nameKanji 角色日文名
 * @property nicknames 昵称列表
 * @property favorites 收藏数
 * @property about 角色介绍
 */
@Serializable
data class Character(
    // 必需字段
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: Images,
    
    @SerialName("name")
    val name: String,
    
    // 可选字段
    @SerialName("name_kanji")
    val nameKanji: String? = null,
    
    @SerialName("nicknames")
    val nicknames: List<String>? = null,
    
    @SerialName("favorites")
    val favorites: Int? = null,
    
    @SerialName("about")
    val about: String? = null,
    
    // 完整信息专属字段
    @SerialName("anime")
    val anime: List<CharacterAnimeEntry>? = null,
    
    @SerialName("manga")
    val manga: List<CharacterMangaEntry>? = null,
    
    @SerialName("voices")
    val voices: List<CharacterVoiceActor>? = null
)

/**
 * 角色动漫出演记录
 * 
 * @property role 角色类型（Main, Supporting等）
 * @property anime 动漫信息
 */
@Serializable
data class CharacterAnimeEntry(
    @SerialName("role")
    val role: String? = null,
    
    @SerialName("anime")
    val anime: AnimeMeta? = null
)

/**
 * 动漫元信息
 * 
 * @property malId MAL ID（必需）
 * @property url MAL URL（必需）
 * @property images 动漫图片（必需）
 * @property title 动漫标题（必需）
 */
@Serializable
data class AnimeMeta(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: Images,
    
    @SerialName("title")
    val title: String
)

/**
 * 角色漫画出现记录
 * 
 * @property role 角色类型（Main, Supporting等）
 * @property manga 漫画信息
 */
@Serializable
data class CharacterMangaEntry(
    @SerialName("role")
    val role: String? = null,
    
    @SerialName("manga")
    val manga: MangaMeta? = null
)

/**
 * 漫画元信息
 * 
 * @property malId MAL ID（必需）
 * @property url MAL URL（必需）
 * @property images 漫画图片（必需）
 * @property title 漫画标题（必需）
 */
@Serializable
data class MangaMeta(
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("images")
    val images: Images,
    
    @SerialName("title")
    val title: String
)

/**
 * 角色声优信息
 * 
 * @property language 配音语言
 * @property person 声优信息
 */
@Serializable
data class CharacterVoiceActor(
    @SerialName("language")
    val language: String? = null,
    
    @SerialName("person")
    val person: PersonMeta? = null
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
 * 角色图片
 * 
 * @property jpg JPG 格式图片
 * @property webp WEBP 格式图片
 */
@Serializable
data class CharacterPicture(
    @SerialName("jpg")
    val jpg: ImageFormat? = null,
    
    @SerialName("webp")
    val webp: ImageFormat? = null
)

