package com.pusu.indexed.shared.feature.search.presentation

import com.pusu.indexed.domain.discover.model.AnimeItem

/**
 * Search 页面的 UI 状态
 * 
 * 使用不可变数据类来表示 UI 的所有可能状态。
 */
data class SearchUiState(
    /**
     * 搜索关键词
     */
    val query: String = "",
    
    /**
     * 是否正在搜索
     */
    val isLoading: Boolean = false,
    
    /**
     * 错误消息（如果有）
     */
    val error: String? = null,
    
    /**
     * 搜索结果列表
     */
    val results: List<AnimeItem> = emptyList(),
    
    /**
     * 是否显示空状态
     */
    val showEmptyState: Boolean = false
) {
    /**
     * 是否有错误
     */
    val hasError: Boolean
        get() = error != null
    
    /**
     * 是否有搜索结果
     */
    val hasResults: Boolean
        get() = results.isNotEmpty()
    
    /**
     * 是否可以搜索（有查询关键词且不在加载中）
     */
    val canSearch: Boolean
        get() = query.isNotBlank() && !isLoading
}

