package com.pusu.indexed.shared.feature.discover.presentation

import com.pusu.indexed.domain.discover.model.AnimeItem

/**
 * Discover 页面的 UI 状态
 * 
 * 使用不可变数据类来表示 UI 的所有可能状态。
 * 这是 MVI（Model-View-Intent）架构的核心部分。
 */
data class DiscoverUiState(
    /**
     * 是否正在加载
     */
    val isLoading: Boolean = false,
    
    /**
     * 错误消息（如果有）
     */
    val error: String? = null,
    
    /**
     * 热门动漫列表
     */
    val trendingAnime: List<AnimeItem> = emptyList(),
    
    /**
     * 本季新番列表
     */
    val currentSeasonAnime: List<AnimeItem> = emptyList(),
    
    /**
     * 排行榜列表
     */
    val topAnime: List<AnimeItem> = emptyList(),
    
    /**
     * 是否正在刷新
     */
    val isRefreshing: Boolean = false
) {
    /**
     * 是否有错误
     */
    val hasError: Boolean
        get() = error != null
    
    /**
     * 是否有内容
     */
    val hasContent: Boolean
        get() = trendingAnime.isNotEmpty() || currentSeasonAnime.isNotEmpty() || topAnime.isNotEmpty()
}

