package com.pusu.indexed.jikan.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Jikan API 通用响应包装类
 * 
 * @param T 数据类型
 * @property data 响应数据
 */
@Serializable
data class JikanResponse<T>(
    @SerialName("data")
    val data: T? = null
)

/**
 * Jikan API 分页响应包装类
 * 
 * @param T 数据项类型
 * @property data 数据列表
 * @property pagination 分页信息
 */
@Serializable
data class JikanPageResponse<T>(
    @SerialName("data")
    val data: List<T>? = null,
    
    @SerialName("pagination")
    val pagination: Pagination? = null
)

/**
 * 分页信息
 * 
 * @property lastVisiblePage 最后可见页码（可选）
 * @property hasNextPage 是否有下一页（必需）
 * @property currentPage 当前页码（可选）
 * @property items 分页项信息（可选）
 */
@Serializable
data class Pagination(
    @SerialName("last_visible_page")
    val lastVisiblePage: Int? = null,
    
    @SerialName("has_next_page")
    val hasNextPage: Boolean = false,
    
    @SerialName("current_page")
    val currentPage: Int? = null,
    
    @SerialName("items")
    val items: PaginationItems? = null
)

/**
 * 分页项详细信息
 * 
 * @property count 当前页项数
 * @property total 总项数
 * @property perPage 每页项数
 */
@Serializable
data class PaginationItems(
    @SerialName("count")
    val count: Int? = null,
    
    @SerialName("total")
    val total: Int? = null,
    
    @SerialName("per_page")
    val perPage: Int? = null
)

