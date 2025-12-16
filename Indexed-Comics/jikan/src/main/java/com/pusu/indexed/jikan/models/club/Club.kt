package com.pusu.indexed.jikan.models.club

import com.pusu.indexed.jikan.models.character.AnimeMeta
import com.pusu.indexed.jikan.models.character.MangaMeta
import com.pusu.indexed.jikan.models.anime.CharacterMeta
import com.pusu.indexed.jikan.models.common.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 俱乐部数据模型
 * 
 * @property malId MAL ID
 * @property name 俱乐部名称
 * @property url MAL URL
 * @property images 俱乐部图片
 * @property members 成员数
 * @property category 分类
 * @property created 创建日期
 * @property access 访问权限
 */
@Serializable
data class Club(
    // 必需字段
    @SerialName("mal_id")
    val malId: Int,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("url")
    val url: String,
    
    // 可选字段
    
    @SerialName("images")
    val images: Images? = null,
    
    @SerialName("members")
    val members: Int? = null,
    
    @SerialName("category")
    val category: String? = null,
    
    @SerialName("created")
    val created: String? = null,
    
    @SerialName("access")
    val access: String? = null
)

/**
 * 俱乐部成员
 * 
 * @property username 用户名
 * @property url 用户主页 URL
 * @property images 用户头像
 */
@Serializable
data class ClubMember(
    @SerialName("username")
    val username: String? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("images")
    val images: Images? = null
)

/**
 * 俱乐部工作人员
 * 
 * @property username 用户名
 * @property url 用户主页 URL
 * @property images 用户头像
 */
@Serializable
data class ClubStaff(
    @SerialName("username")
    val username: String? = null,
    
    @SerialName("url")
    val url: String? = null,
    
    @SerialName("images")
    val images: Images? = null
)

/**
 * 俱乐部关系信息
 * 
 * @property anime 相关动漫列表
 * @property manga 相关漫画列表
 * @property characters 相关角色列表
 */
@Serializable
data class ClubRelations(
    @SerialName("anime")
    val anime: List<AnimeMeta>? = null,
    
    @SerialName("manga")
    val manga: List<MangaMeta>? = null,
    
    @SerialName("characters")
    val characters: List<CharacterMeta>? = null
)

