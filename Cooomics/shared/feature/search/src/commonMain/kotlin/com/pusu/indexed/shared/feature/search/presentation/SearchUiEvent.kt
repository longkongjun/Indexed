package com.pusu.indexed.shared.feature.search.presentation

/**
 * Search 页面的 UI 事件
 * 
 * 表示需要 UI 层响应的一次性事件。
 */
sealed interface SearchUiEvent {
    /**
     * 显示错误消息
     */
    data class ShowError(val message: String) : SearchUiEvent
    
    /**
     * 导航到动漫详情页
     */
    data class NavigateToDetail(val animeId: Int) : SearchUiEvent
}

