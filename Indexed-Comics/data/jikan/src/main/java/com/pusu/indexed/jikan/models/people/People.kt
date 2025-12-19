package com.pusu.indexed.jikan.models.people

import com.pusu.indexed.jikan.models.common.*
import com.pusu.indexed.jikan.models.character.AnimeMeta
import com.pusu.indexed.jikan.models.character.MangaMeta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 人物数据模型（声优/制作人员）
 * 
 * @property malId MyAnimeList ID
 * @property url MAL 页面 URL
 * @property websiteUrl 官方网站 URL
 * @property images 图片集合
 * @property name 人物名称
 * @property givenName 名
 * @property familyName 姓
 * @property alternateNames 别名列表
 * @property birthday 生日
 * @property favorites 收藏数
 * @property about 人物介绍
 */
@Serializable
data class Person(
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
    @SerialName("website_url")
    val websiteUrl: String? = null,
    
    @SerialName("given_name")
    val givenName: String? = null,
    
    @SerialName("family_name")
    val familyName: String? = null,
    
    @SerialName("alternate_names")
    val alternateNames: List<String>? = null,
    
    @SerialName("birthday")
    val birthday: String? = null,
    
    @SerialName("favorites")
    val favorites: Int? = null,
    
    @SerialName("about")
    val about: String? = null,
    
    // 完整信息专属字段
    @SerialName("anime")
    val anime: List<PersonAnimeEntry>? = null,
    
    @SerialName("manga")
    val manga: List<PersonMangaEntry>? = null,
    
    @SerialName("voices")
    val voices: List<PersonVoiceRole>? = null
)

/**
 * 人物动漫参与记录
 * 
 * @property position 职位
 * @property anime 动漫信息
 */
@Serializable
data class PersonAnimeEntry(
    @SerialName("position")
    val position: String? = null,
    
    @SerialName("anime")
    val anime: AnimeMeta? = null
)

/**
 * 人物漫画参与记录
 * 
 * @property position 职位
 * @property manga 漫画信息
 */
@Serializable
data class PersonMangaEntry(
    @SerialName("position")
    val position: String? = null,
    
    @SerialName("manga")
    val manga: MangaMeta? = null
)

/**
 * 人物配音角色记录
 * 
 * @property role 角色类型
 * @property anime 动漫信息
 * @property character 角色信息
 */
@Serializable
data class PersonVoiceRole(
    @SerialName("role")
    val role: String? = null,
    
    @SerialName("anime")
    val anime: AnimeMeta? = null,
    
    @SerialName("character")
    val character: CharacterMeta? = null
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
 * 人物图片
 * 
 * @property jpg JPG 格式图片
 * @property webp WEBP 格式图片
 */
@Serializable
data class PersonPicture(
    @SerialName("jpg")
    val jpg: ImageFormat? = null,
    
    @SerialName("webp")
    val webp: ImageFormat? = null
)

