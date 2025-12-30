package com.pusu.indexed.shared.feature.animedetail.animelist.presentation

import com.pusu.indexed.domain.discover.model.AnimeItem

/**
 * AnimeList 页面的 UI 状态
 */
data class AnimeListUiState(
    // 列表数据
    val animeList: List<AnimeItem> = emptyList(),
    
    // 分页信息
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    
    // 加载状态
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    
    // 错误状态
    val error: String? = null,
    
    // 列表类型和标题
    val listType: AnimeListType = AnimeListType.Trending,
    val title: String = ""
) {
    /**
     * 是否有内容
     */
    val hasContent: Boolean
        get() = animeList.isNotEmpty()
    
    /**
     * 是否有错误
     */
    val hasError: Boolean
        get() = error != null
    
    /**
     * 是否显示空状态
     */
    val showEmptyState: Boolean
        get() = !isLoading && !hasContent && !hasError
}

/**
 * 动漫列表类型
 */
enum class AnimeListType {
    Trending,       // 热门动漫
    CurrentSeason,  // 本季新番
    TopRanked       // 排行榜
}

